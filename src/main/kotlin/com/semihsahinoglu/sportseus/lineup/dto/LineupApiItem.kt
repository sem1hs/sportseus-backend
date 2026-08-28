package com.semihsahinoglu.sportseus.lineup.dto

data class LineupApiItem(
    val team: LineupTeamNode? = null,
    val coach: LineupCoachNode? = null,
    val formation: String? = null,
    val startXI: List<LineupPlayerWrapper> = emptyList(),
    val substitutes: List<LineupPlayerWrapper> = emptyList(),
)