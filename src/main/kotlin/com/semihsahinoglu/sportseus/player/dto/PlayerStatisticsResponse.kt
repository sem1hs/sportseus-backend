package com.semihsahinoglu.sportseus.player.dto

import com.semihsahinoglu.sportseus.player.dto.statistic.PlayerStatisticsNode
import java.util.UUID

data class PlayerStatisticsResponse(
    val id: UUID,                       // kaydın kimliği (silme için)
    val season: Int,
    val team: StatTeamSummary,
    val league: StatLeagueSummary,
    val statistics: PlayerStatisticsNode,
)