package com.semihsahinoglu.sportseus.player.dto

import com.semihsahinoglu.sportseus.player.dto.statistic.GamesNode
import com.semihsahinoglu.sportseus.player.dto.statistic.GoalsNode
import java.util.UUID

data class PlayerStatisticsUpdateRequest(
    val playerId: UUID? = null,
    val teamExternalId: Int? = null,
    val leagueExternalId: Int? = null,
    val season: Int? = null,
    val games: GamesNode? = null,
    val goals: GoalsNode? = null,
)
