package com.semihsahinoglu.sportseus.team.service

import com.semihsahinoglu.sportseus.league.service.LeagueService
import com.semihsahinoglu.sportseus.team.client.TeamStatisticsApiClient
import com.semihsahinoglu.sportseus.team.dto.statistics.TeamStatisticsCreateRequest
import com.semihsahinoglu.sportseus.team.dto.statistics.TeamStatisticsNode
import com.semihsahinoglu.sportseus.team.dto.statistics.TeamStatisticsResponse
import com.semihsahinoglu.sportseus.team.dto.statistics.TeamStatisticsUpdateRequest
import com.semihsahinoglu.sportseus.team.entity.TeamStatistics
import com.semihsahinoglu.sportseus.team.exception.TeamStatisticsConflictException
import com.semihsahinoglu.sportseus.team.exception.TeamStatisticsNotFoundException
import com.semihsahinoglu.sportseus.team.mapper.TeamStatisticsMapper
import com.semihsahinoglu.sportseus.team.mapper.TeamStatisticsMerger
import com.semihsahinoglu.sportseus.team.repository.TeamStatisticsRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.json.JsonMapper
import java.util.UUID

@Service
class TeamStatisticsService(
    private val teamStatisticsApiClient: TeamStatisticsApiClient,
    private val teamStatisticsRepository: TeamStatisticsRepository,
    private val teamService: TeamService,
    private val leagueService: LeagueService,
    private val jsonMapper: JsonMapper,   // Boot 4 Jackson 3 — otomatik yapılandırılmış bean
    private val teamStatisticsMapper: TeamStatisticsMapper,
    private val teamStatisticsMerger: TeamStatisticsMerger
) {
    // ADMIN: istatistiği API'den çek, jsonb'ye yaz (katı ön koşul)
    @Transactional
    fun syncStatistics(teamExternalId: Int, leagueExternalId: Int, season: Int): TeamStatisticsResponse {
        // KATI ÖN KOŞUL: takım ve lig DB'de olmalı
        val team = teamService.findByExternalId(teamExternalId)
        val league = leagueService.getByExternalIdAndSeasonEntity(leagueExternalId, season)
        val stats = teamStatisticsApiClient.fetchStatistics(teamExternalId, leagueExternalId, season)
            ?: throw TeamStatisticsNotFoundException("API-Football'da istatistik bulunamadı: team=$teamExternalId league=$leagueExternalId season=$season")

        val json = jsonMapper.writeValueAsString(stats)   // DTO → String (jsonb'ye)
        val existing = teamStatisticsRepository.findByTeamIdAndLeagueIdAndSeason(team.id!!, league.id!!, season)

        val saved = when {
            existing == null -> teamStatisticsRepository.save(
                TeamStatistics(team = team, league = league, season = season, stats = json)
            )

            existing.manualAdded || existing.manuallyEdited -> existing
            else -> {
                existing.stats = json
                teamStatisticsRepository.save(existing)
            }
        }

        return teamStatisticsMapper.toResponse(saved, stats)
    }

    // ADMIN: elle team istatistiği ekle (team+league external, çakışma 409)
    @Transactional
    fun create(request: TeamStatisticsCreateRequest): TeamStatisticsResponse {
        val team = teamService.findByExternalId(request.teamExternalId)
        val league = leagueService.getByExternalIdAndSeasonEntity(request.leagueExternalId, request.season)

        // composite çakışma
        val exists = teamStatisticsRepository
            .findByTeamIdAndLeagueIdAndSeason(team.id!!, league.id!!, request.season) != null
        if (exists) throw TeamStatisticsConflictException("Bu istatistik zaten var: team=${request.teamExternalId} league=${request.leagueExternalId} season=${request.season}")

        // node → jsonb serialize (sync ile aynı)
        val jsonString = jsonMapper.writeValueAsString(request.statistics)

        val teamStatistics = teamStatisticsMapper.toEntity(team, league, request.season, jsonString)
        val saved = teamStatisticsRepository.save(teamStatistics)
        val json = jsonMapper.readValue(saved.stats, TeamStatisticsNode::class.java)
        return teamStatisticsMapper.toResponse(saved, json)
    }

    // ADMIN: elle güncelleme
    @Transactional
    fun update(id: UUID, request: TeamStatisticsUpdateRequest): TeamStatisticsResponse {
        val stat = teamStatisticsRepository.findById(id)
            .orElseThrow { TeamStatisticsNotFoundException("İstatistik bulunamadı: id=$id") }

        val team = request.teamExternalId?.let { extId ->
            teamService.findByExternalId(extId)
        }

        // league — verilmişse çöz (season'a bağlı)
        val league = request.leagueExternalId?.let { extId ->
            val season = request.season ?: stat.season
            leagueService.getByExternalIdAndSeasonEntity(extId, season)
        }

        // stats — verilmişse merge (jsonMapper + merger, service'in işi)
        val mergedStats = request.statistics?.let { incoming ->
            val existing = jsonMapper.readValue(stat.stats, TeamStatisticsNode::class.java)
            val merged = teamStatisticsMerger.merge(existing, incoming)
            jsonMapper.writeValueAsString(merged)
        }

        // entity partial update — çözülmüş/merge edilmiş parçaları geçir
        stat.applyManualUpdate(
            team = team,
            league = league,
            season = request.season,
            mergedStats = mergedStats,
        )
        return try {
            val saved = teamStatisticsRepository.saveAndFlush(stat)
            val node = jsonMapper.readValue(saved.stats, TeamStatisticsNode::class.java)
            teamStatisticsMapper.toResponse(saved, node)
        } catch (e: DataIntegrityViolationException) {
            throw TeamStatisticsConflictException(
                "Bu güncelleme mevcut bir istatistikle çakışıyor (aynı team+league+season): id=$id"
            )
        }
    }

    // ADMIN: hard delete
    @Transactional
    fun deleteTeamStatistics(id: UUID) {
        if (!teamStatisticsRepository.existsById(id)) throw TeamStatisticsNotFoundException("Takım istatistiği bulunamadı: $id")
        teamStatisticsRepository.deleteById(id)
    }

    // PUBLIC: DB'den oku, String → DTO
    @Transactional(readOnly = true)
    fun getStatistics(teamId: UUID, leagueId: UUID, season: Int): TeamStatisticsResponse {
        val entity = teamStatisticsRepository.findByTeamIdAndLeagueIdAndSeason(teamId, leagueId, season)
            ?: throw TeamStatisticsNotFoundException("İstatistik bulunamadı")
        val json = jsonMapper.readValue(entity.stats, TeamStatisticsNode::class.java)
        return teamStatisticsMapper.toResponse(entity, json)
    }

    // PUBLIC: DB'den oku, String → DTO
    @Transactional(readOnly = true)
    fun getStatistics(teamId: UUID, season: Int): TeamStatisticsResponse {
        val entity = teamStatisticsRepository.findByTeamIdAndSeason(teamId, season)
            ?: throw TeamStatisticsNotFoundException("İstatistik bulunamadı")
        val json = jsonMapper.readValue(entity.stats, TeamStatisticsNode::class.java)
        return teamStatisticsMapper.toResponse(entity, json)
    }
}