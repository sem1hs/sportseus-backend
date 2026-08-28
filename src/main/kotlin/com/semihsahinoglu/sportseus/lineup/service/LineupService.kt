package com.semihsahinoglu.sportseus.lineup.service

import com.semihsahinoglu.sportseus.fixture.entity.Fixture
import com.semihsahinoglu.sportseus.fixture.service.FixtureService
import com.semihsahinoglu.sportseus.lineup.dto.LineupApiItem
import com.semihsahinoglu.sportseus.lineup.dto.LineupResponse
import com.semihsahinoglu.sportseus.lineup.dto.LineupUpdateRequest
import com.semihsahinoglu.sportseus.lineup.entity.FixtureLineup
import com.semihsahinoglu.sportseus.lineup.exception.LineupNotFoundException
import com.semihsahinoglu.sportseus.lineup.mapper.LineupMapper
import com.semihsahinoglu.sportseus.lineup.repository.FixtureLineupRepository
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

    // ADMIN: elle güncelleme (formation/coach partial + players replace + manuallyEdited)
    @Transactional
    fun update(fixtureExternalId: Long, teamExternalId: Int, request: LineupUpdateRequest): LineupResponse {
        val lineup = fixtureLineupRepository.findByFixtureExternalIdAndTeamExternalId(fixtureExternalId, teamExternalId)
            ?: throw LineupNotFoundException("Lineup bulunamadı: fixture=$fixtureExternalId team=$teamExternalId")

        // formation/coach partial
        lineupMapper.applyManualUpdate(lineup, request)
        lineup.makeManuallyEdited()

        return lineupMapper.toResponse(fixtureLineupRepository.save(lineup))
    }


    // PUBLIC: bir maçın dizilişleri (home + away)
    @Transactional(readOnly = true)
    fun getByFixture(fixtureExternalId: Long): List<LineupResponse> =
        fixtureLineupRepository.findAllByFixtureExternalId(fixtureExternalId)
            .map(lineupMapper::toResponse)

    // ADMIN: tekil silme
    @Transactional
    fun deleteById(id: UUID) {
        if (!fixtureLineupRepository.existsById(id)) throw LineupNotFoundException("Lineup bulunamadı: id=$id")
        fixtureLineupRepository.deleteById(id)
    }
}