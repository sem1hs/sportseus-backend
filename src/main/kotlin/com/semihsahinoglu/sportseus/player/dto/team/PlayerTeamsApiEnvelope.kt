package com.semihsahinoglu.sportseus.player.dto.team

data class PlayerTeamsApiEnvelope(
    val response: List<PlayerTeamsApiItem> = emptyList(),
)