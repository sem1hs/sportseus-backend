package com.semihsahinoglu.sportseus.coach.dto

import java.time.LocalDate
import java.util.UUID

data class CoachCareerResponse(
    val id: UUID,
    val teamExternalId: Int,
    val teamName: String,
    val teamLogo: String?,
    val startDate: LocalDate,
    val endDate: LocalDate?,                  // null = hâlâ görevde
)