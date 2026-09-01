package com.semihsahinoglu.sportseus.team.dto.statistics

import java.util.UUID

data class TeamStatisticsResponse(
    val id: UUID,
    val season: Int,
    val team: StatTeamSummary,
    val league: StatLeagueSummary,
    val statistics: TeamStatisticsNode,
)
