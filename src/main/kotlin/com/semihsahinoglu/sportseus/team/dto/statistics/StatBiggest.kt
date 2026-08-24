package com.semihsahinoglu.sportseus.team.dto.statistics

data class StatBiggest(
    val streak: Streak? = null,
    val wins: HomeAwayStr? = null,      // "5-0"
    val loses: HomeAwayStr? = null,
    val goals: BiggestGoals? = null,
)
