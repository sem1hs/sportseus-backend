package com.semihsahinoglu.sportseus.league.dto

data class LeagueByTeamApiEnvelope(
    val response: List<LeagueByTeamApiItem> = emptyList(),
)