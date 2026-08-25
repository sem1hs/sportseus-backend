package com.semihsahinoglu.sportseus.player.dto.statistic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class PlayerStatisticsNode(
    val games: GamesNode? = null,
    val goals: GoalsNode? = null,
)