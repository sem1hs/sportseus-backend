package com.semihsahinoglu.sportseus.player.dto.api

data class PlayerApiEnvelope(
    val response: List<PlayerApiItem> = emptyList(),
)