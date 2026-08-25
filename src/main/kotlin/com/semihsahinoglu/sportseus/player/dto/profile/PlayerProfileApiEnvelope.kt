package com.semihsahinoglu.sportseus.player.dto.profile

data class PlayerProfileApiEnvelope(
    val response: List<PlayerProfileApiItem> = emptyList(),
)