package com.semihsahinoglu.sportseus.standing.dto

data class StandingApiEnvelope(
    val response: List<StandingApiItem> = emptyList(),
)