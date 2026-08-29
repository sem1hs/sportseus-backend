package com.semihsahinoglu.sportseus.coach.dto

import java.time.LocalDate

data class CoachCareerUpdateRequest(
    val teamExternalId: Int? = null,
    val teamName: String? = null,
    val teamLogo: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
)
