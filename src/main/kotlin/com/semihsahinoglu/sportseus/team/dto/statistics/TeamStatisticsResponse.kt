package com.semihsahinoglu.sportseus.team.dto.statistics

import java.util.UUID

data class TeamStatisticsResponse(
    val id: UUID,
    val statistics: TeamStatisticsNode,
)
