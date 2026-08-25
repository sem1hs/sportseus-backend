package com.semihsahinoglu.sportseus.player.dto.api

import com.semihsahinoglu.sportseus.player.dto.PlayerNode
import com.semihsahinoglu.sportseus.player.dto.statistic.PlayerStatItem

data class PlayerApiItem(
    val player: PlayerNode? = null,
    val statistics: List<PlayerStatItem> = emptyList(),
)