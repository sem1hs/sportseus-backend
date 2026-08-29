package com.semihsahinoglu.sportseus.standing.dto

data class StandingLeagueNode(
    val id: Int? = null,
    val season: Int? = null,
    val standings: List<List<StandingRowNode>> = emptyList(),
)
