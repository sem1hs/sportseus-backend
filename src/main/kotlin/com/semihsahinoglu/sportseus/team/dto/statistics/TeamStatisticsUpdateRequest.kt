package com.semihsahinoglu.sportseus.team.dto.statistics

data class TeamStatisticsUpdateRequest(
    val teamExternalId: Int? = null,
    val leagueExternalId: Int? = null,
    val season: Int? = null,
    val statistics: TeamStatisticsNode? = null,
)
