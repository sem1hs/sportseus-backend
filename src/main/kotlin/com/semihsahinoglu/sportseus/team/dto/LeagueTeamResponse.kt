package com.semihsahinoglu.sportseus.team.dto

import java.util.UUID

data class LeagueTeamResponse(
    val id: UUID?,
    val season: Int,
    val league: LeagueTeamLeagueSummary,
    val team: LeagueTeamTeamSummary
)
