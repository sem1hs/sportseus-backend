package com.semihsahinoglu.sportseus.coach.dto

data class CoachApiEnvelope(
    val response: List<CoachApiItem> = emptyList(),
)