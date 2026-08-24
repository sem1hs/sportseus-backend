package com.semihsahinoglu.sportseus.team.dto.statistics

data class StatCards(
    val yellow: Map<String, MinuteBucket> = emptyMap(),
    val red: Map<String, MinuteBucket> = emptyMap(),
)
