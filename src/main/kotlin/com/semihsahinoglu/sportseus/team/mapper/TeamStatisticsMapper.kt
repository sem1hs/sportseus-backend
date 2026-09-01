package com.semihsahinoglu.sportseus.team.mapper

import com.semihsahinoglu.sportseus.league.entity.League
import com.semihsahinoglu.sportseus.team.dto.statistics.StatLeagueSummary
import com.semihsahinoglu.sportseus.team.dto.statistics.StatTeamSummary
import com.semihsahinoglu.sportseus.team.dto.statistics.TeamStatisticsNode
import com.semihsahinoglu.sportseus.team.dto.statistics.TeamStatisticsResponse
import com.semihsahinoglu.sportseus.team.entity.Team
import com.semihsahinoglu.sportseus.team.entity.TeamStatistics
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class TeamStatisticsMapper {

    fun toEntity(team: Team, league: League, season: Int, json: String): TeamStatistics = TeamStatistics(
        team = team,
        league = league,
        season = season,
        stats = json,
        manualAdded = true,
        manuallyEdited = false,
    )

    fun toResponse(teamStatistics: TeamStatistics, statistics: TeamStatisticsNode): TeamStatisticsResponse =
        TeamStatisticsResponse(
            id = teamStatistics.id!!,
            season = teamStatistics.season,
            team = toStatTeamSummary(teamStatistics),
            league = toStatLeagueSummary(teamStatistics),
            statistics = statistics,
        )

    private fun toStatTeamSummary(entity: TeamStatistics): StatTeamSummary = StatTeamSummary(
        id = entity.team.id!!,
        externalId = entity.team.externalId,
        name = entity.team.name,
        logoUrl = entity.team.logoUrl,
    )

    private fun toStatLeagueSummary(entity: TeamStatistics): StatLeagueSummary = StatLeagueSummary(
        id = entity.league.id!!,
        externalId = entity.league.externalId,
        name = entity.league.name,
        logoUrl = entity.league.logoUrl,
    )
}