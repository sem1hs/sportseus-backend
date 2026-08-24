package com.semihsahinoglu.sportseus.team.dto.statistics

data class StatPenalty(
    val scored: TotalPercentage? = null,
    val missed: TotalPercentage? = null,
    val total: Int? = null,
)
