package com.semihsahinoglu.sportseus.lineup.dto

data class LineupApiEnvelope(
    val response: List<LineupApiItem> = emptyList()
)
