package com.semihsahinoglu.sportseus.player.dto

import com.semihsahinoglu.sportseus.player.dto.statistic.GamesNode
import com.semihsahinoglu.sportseus.player.dto.statistic.GoalsNode
import java.util.UUID

data class PlayerStatisticsCreateRequest(
    val playerId: UUID,             // player UUID (elle-player olabilir)
    val teamExternalId: Int,        // team external id
    val leagueExternalId: Int,      // league external id
    val season: Int,
    val games: GamesNode? = null,   // sync'teki node reuse (appearances, rating...)
    val goals: GoalsNode? = null,
)
