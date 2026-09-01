package com.semihsahinoglu.sportseus.team.mapper

import com.semihsahinoglu.sportseus.league.entity.League
import com.semihsahinoglu.sportseus.team.dto.LeagueTeamLeagueSummary
import com.semihsahinoglu.sportseus.team.dto.LeagueTeamResponse
import com.semihsahinoglu.sportseus.team.dto.LeagueTeamTeamSummary
import com.semihsahinoglu.sportseus.team.entity.LeagueTeam
import com.semihsahinoglu.sportseus.team.entity.Team
import org.springframework.stereotype.Component

@Component
class LeagueTeamMapper {

    fun toEntity(league: League, team: Team, season: Int): LeagueTeam = LeagueTeam(
        league = league,
        team = team,
        season = season,
        manualAdded = true,
        manuallyEdited = false,
    )

    fun toResponse(leagueTeam: LeagueTeam): LeagueTeamResponse = LeagueTeamResponse(
        id = leagueTeam.id,
        season = leagueTeam.season,
        league = toLeagueTeamLeagueResponse(leagueTeam.league),
        team = toLeagueTeamTeamResponse(leagueTeam.team),
    )

    private fun toLeagueTeamLeagueResponse(league: League): LeagueTeamLeagueSummary = LeagueTeamLeagueSummary(
        id = league.id,
        externalId = league.externalId,
        name = league.name,
        logoUrl = league.logoUrl
    )

    private fun toLeagueTeamTeamResponse(team: Team): LeagueTeamTeamSummary = LeagueTeamTeamSummary(
        id = team.id,
        externalId = team.externalId,
        name = team.name,
        logoUrl = team.logoUrl
    )
}