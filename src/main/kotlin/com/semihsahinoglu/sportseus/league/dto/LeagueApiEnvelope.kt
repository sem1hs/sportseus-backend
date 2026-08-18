package com.semihsahinoglu.sportseus.league.dto

data class LeagueApiEnvelope(
    val response: List<LeagueApiItem> = emptyList(),
)
