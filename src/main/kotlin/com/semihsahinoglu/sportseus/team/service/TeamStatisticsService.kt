package com.semihsahinoglu.sportseus.team.service

import com.semihsahinoglu.sportseus.league.service.LeagueService
import com.semihsahinoglu.sportseus.team.client.TeamStatisticsApiClient
import com.semihsahinoglu.sportseus.team.dto.statistics.TeamStatisticsNode
import com.semihsahinoglu.sportseus.team.dto.statistics.TeamStatisticsResponse
import com.semihsahinoglu.sportseus.team.entity.TeamStatistics
import com.semihsahinoglu.sportseus.team.exception.TeamStatisticsNotFoundException
import com.semihsahinoglu.sportseus.team.repository.TeamStatisticsRepository
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

        val saved = if (existing != null) {
            existing.stats = json
            teamStatisticsRepository.save(existing)
        } else {
            teamStatisticsRepository.save(TeamStatistics(team = team, league = league, season = season, stats = json))
        }

        return TeamStatisticsResponse(id = saved.id!!, statistics = stats)
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
        return TeamStatisticsResponse(
            id = entity.id!!,
            statistics = jsonMapper.readValue(entity.stats, TeamStatisticsNode::class.java)
        )   // String → DTO
    }
}