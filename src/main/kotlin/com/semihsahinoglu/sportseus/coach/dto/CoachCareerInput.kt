package com.semihsahinoglu.sportseus.coach.dto

import java.time.LocalDate

data class CoachCareerInput(
    val teamExternalId: Int,
    val teamName: String,
    val teamLogo: String? = null,
    val startDate: LocalDate,
    val endDate: LocalDate? = null,
)
