package com.semihsahinoglu.sportseus.standing.dto

data class StandingStatsNode(
    val played: Int? = null,
    val win: Int? = null,
    val draw: Int? = null,
    val lose: Int? = null,
    val goals: StandingGoalsNode? = null,
)
