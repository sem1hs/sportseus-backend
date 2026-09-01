package com.semihsahinoglu.sportseus.team.dto.statistics

data class TeamStatisticsCreateRequest(
    val teamExternalId: Int,
    val leagueExternalId: Int,
    val season: Int,
    val statistics: TeamStatisticsNode,
)