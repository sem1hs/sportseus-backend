package com.semihsahinoglu.sportseus.lineup.service

import com.semihsahinoglu.sportseus.fixture.entity.Fixture
import com.semihsahinoglu.sportseus.fixture.service.FixtureService
import com.semihsahinoglu.sportseus.lineup.dto.LineupApiItem
import com.semihsahinoglu.sportseus.lineup.dto.LineupCreateRequest
import com.semihsahinoglu.sportseus.lineup.dto.LineupResponse
import com.semihsahinoglu.sportseus.lineup.dto.LineupUpdateRequest
import com.semihsahinoglu.sportseus.lineup.entity.FixtureLineup
import com.semihsahinoglu.sportseus.lineup.exception.LineupConflictException
import com.semihsahinoglu.sportseus.lineup.exception.LineupNotFoundException
import com.semihsahinoglu.sportseus.lineup.mapper.LineupMapper
import com.semihsahinoglu.sportseus.lineup.repository.FixtureLineupRepository
import com.semihsahinoglu.sportseus.team.exception.TeamNotFoundException
import com.semihsahinoglu.sportseus.team.service.TeamService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class LineupService(
    private val fixtureLineupRepository: FixtureLineupRepository,
    private val fixtureService: FixtureService,
    private val teamService: TeamService,
    private val lineupMapper: LineupMapper,
) {
    // METHOD: tek lineup upsert (LineupFixtureSyncService döngüden çağırır)
    @Transactional
    fun syncOneLineup(fixture: Fixture, item: LineupApiItem): LineupResponse? {
        // team çöz — DB'de yoksa bu lineup'ı ATLA (null döndür, döngü elesin)
        val teamExtId = item.team?.id ?: return null
        val team = teamService.findByExternalIdOptional(teamExtId) ?: return null

        // upsert lineup başlığı
        val existing = fixtureLineupRepository.findByFixtureIdAndTeamId(fixture.id!!, team.id!!)
        if (existing != null && (existing.manuallyEdited || existing.manualAdded)) return lineupMapper.toResponse(
            existing
        )

        val lineup = if (existing != null) {
            // manuallyEdited → KOMPLE atla (seçenek B): players dahil hiç dokunma
            if (existing.manuallyEdited) return lineupMapper.toResponse(existing)

            lineupMapper.applyApiData(existing, item)
            existing
        } else lineupMapper.toEntity(item, fixture, team)

        // players sil-yeniden-yaz (orphanRemoval)
        fillPlayers(lineup, item)

        val saved = fixtureLineupRepository.save(lineup)
        return lineupMapper.toResponse(saved)
    }

    // METHOD: players clear + fill (orphanRemoval yönetimi)
    private fun fillPlayers(lineup: FixtureLineup, item: LineupApiItem) {
        lineup.players.clear()   // orphanRemoval eskiyi siler

        item.startXI.forEach { wrapper ->
            wrapper.player?.id?.let {
                lineup.players.add(lineupMapper.toPlayerEntity(lineup, wrapper.player, isStarter = true))
            }
        }
        item.substitutes.forEach { wrapper ->
            wrapper.player?.id?.let {
                lineup.players.add(lineupMapper.toPlayerEntity(lineup, wrapper.player, isStarter = false))
            }
        }
    }

    // ADMIN: elle lineup oluştur (katı FK: fixture + team)
    @Transactional
    fun create(request: LineupCreateRequest): LineupResponse {
        // fixture UUID ile — hem sync-fixture hem elle-fixture bulunur
        val fixture = fixtureService.getByIdEntity(request.fixtureId)

        // team external id ile (katı)
        val team = teamService.findByExternalIdOptional(request.teamExternalId)
            ?: throw TeamNotFoundException("Takım bulunamadı: team=${request.teamExternalId}")

        // (fixture, team) çakışma
        val exists = fixtureLineupRepository.findByFixtureIdAndTeamId(fixture.id!!, team.id!!) != null
        if (exists) throw LineupConflictException("Bu maç+takım için lineup zaten var: fixtureId=${request.fixtureId} team=${request.teamExternalId}")

        val lineup = lineupMapper.toEntity(fixture, team, request)

        request.startXI.forEach { input ->
            lineup.players.add(lineupMapper.toPlayerEntity(lineup, input, isStarter = true))
        }
        request.substitutes.forEach { input ->
            lineup.players.add(lineupMapper.toPlayerEntity(lineup, input, isStarter = false))
        }

        return lineupMapper.toResponse(fixtureLineupRepository.save(lineup))
    }

    // ADMIN: elle güncelleme (formation/coach partial + players replace + manuallyEdited)
    @Transactional
    fun update(fixtureId: UUID, teamExternalId: Int, request: LineupUpdateRequest): LineupResponse {
        val lineup = fixtureLineupRepository.findByFixtureIdAndTeamExternalId(fixtureId, teamExternalId)
            ?: throw LineupNotFoundException("Lineup bulunamadı: fixture=$fixtureId team=$teamExternalId")

        // formation/coach partial
        lineupMapper.applyManualUpdate(lineup, request)
        lineup.makeManuallyEdited()

        return lineupMapper.toResponse(fixtureLineupRepository.save(lineup))
    }


    // PUBLIC: bir maçın dizilişleri (home + away)
    @Transactional(readOnly = true)
    fun getByFixture(fixtureId: UUID): List<LineupResponse> {
        val lineups = fixtureLineupRepository.findAllByFixtureId(fixtureId)
        if (lineups.isEmpty()) return emptyList()

        val homeTeamId = lineups.first().fixture.homeTeam.id
        return lineups
            .sortedByDescending { it.team.id == homeTeamId }
            .map(lineupMapper::toResponse)
    }


    // ADMIN: tekil silme
    @Transactional
    fun deleteById(id: UUID) {
        if (!fixtureLineupRepository.existsById(id)) throw LineupNotFoundException("Lineup bulunamadı: id=$id")
        fixtureLineupRepository.deleteById(id)
    }
}