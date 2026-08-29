package com.semihsahinoglu.sportseus.standing.service

import com.semihsahinoglu.sportseus.league.service.LeagueService
import com.semihsahinoglu.sportseus.standing.client.StandingApiClient
import com.semihsahinoglu.sportseus.standing.dto.StandingResponse
import com.semihsahinoglu.sportseus.standing.dto.StandingRowNode
import com.semihsahinoglu.sportseus.standing.dto.StandingUpdateRequest
import com.semihsahinoglu.sportseus.standing.exception.StandingNotFoundException
import com.semihsahinoglu.sportseus.standing.mapper.StandingMapper
import com.semihsahinoglu.sportseus.standing.repository.StandingRepository
import com.semihsahinoglu.sportseus.team.service.TeamService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class StandingService(
    private val standingRepository: StandingRepository,
    private val standingApiClient: StandingApiClient,
    private val standingMapper: StandingMapper,
    private val leagueService: LeagueService,
    private val teamService: TeamService
) {
    // ADMIN: ligin puan tablosunu sync (tek transaction, team-yok satır atlanır)
    @Transactional
    fun sync(leagueExternalId: Int, season: Int): List<StandingResponse> {
        val league = leagueService.getByExternalIdAndSeasonEntity(leagueExternalId, season)

        val item = standingApiClient.fetchByLeagueAndSeason(leagueExternalId, season) ?: return emptyList()

        // ÇİFT DİZİYİ DÜZLEŞTİR — gruplar × satırlar → tek liste
        val rows: List<StandingRowNode> =
            item.league?.standings?.flatten()?.filter { it.team?.id != null } ?: emptyList()

        // her satır: team çöz → yoksa ATLA+logla (esnek), varsa upsert
        return rows.mapNotNull { row ->
            val teamExtId = row.team!!.id!!
            val team = teamService.findByExternalIdOptional(teamExtId) ?: return@mapNotNull null

            val existing = standingRepository.findByLeagueIdAndTeamIdAndSeason(league.id!!, team.id!!, season)
            val saved = when {
                existing == null -> standingRepository.save(standingMapper.toEntity(row, league, team, season))
                existing.manuallyEdited -> existing
                else -> {
                    standingMapper.applyApiData(existing, row)
                    standingRepository.save(existing)
                }
            }
            standingMapper.toResponse(saved)
        }
    }

    // ADMIN: elle güncelleme (partial, manuallyEdited=true)
    @Transactional
    fun update(id: UUID, request: StandingUpdateRequest): StandingResponse {
        val standing =
            standingRepository.findById(id).orElseThrow { StandingNotFoundException("Sıralama bulunamadı: id=$id") }

        standing.applyManualUpdate(
            rank = request.rank,
            points = request.points,
            goalsDiff = request.goalsDiff,
            group = request.group,
            form = request.form,
            status = request.status,
            description = request.description,
            all = request.all?.toStats(),
            home = request.home?.toStats(),
            away = request.away?.toStats(),
        )
        return standingMapper.toResponse(standingRepository.save(standing))
    }

    // PUBLIC: bir ligin puan tablosu (rank sırasına göre)
    @Transactional(readOnly = true)
    fun getByLeagueAndSeason(leagueExternalId: Int, season: Int): List<StandingResponse> =
        standingRepository.findAllByLeagueExternalIdAndSeasonOrderByRankAsc(leagueExternalId, season)
            .map(standingMapper::toResponse)

    // PUBLIC: bir takımın bir ligdeki sıralaması (tek satır)
    @Transactional(readOnly = true)
    fun getByTeam(teamExternalId: Int, leagueExternalId: Int, season: Int): StandingResponse {
        val standing = standingRepository
            .findByTeamExternalIdAndLeagueExternalIdAndSeason(teamExternalId, leagueExternalId, season)
            ?: throw StandingNotFoundException(
                "Sıralama bulunamadı: team=$teamExternalId league=$leagueExternalId season=$season"
            )
        return standingMapper.toResponse(standing)
    }

    // ADMIN: tekil silme
    @Transactional
    fun deleteById(id: UUID) {
        if (!standingRepository.existsById(id)) throw StandingNotFoundException("Sıralama bulunamadı: id=$id")
        standingRepository.deleteById(id)
    }
}