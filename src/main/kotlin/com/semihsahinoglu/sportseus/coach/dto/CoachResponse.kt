package com.semihsahinoglu.sportseus.coach.dto

import java.time.LocalDate
import java.util.UUID

data class CoachResponse(
    val id: UUID,
    val externalId: Int,
    val name: String,
    val firstName: String?,
    val lastName: String?,
    val age: Int?,
    val birthDate: LocalDate?,
    val birthPlace: String?,
    val birthCountry: String?,
    val nationality: String?,
    val height: String?,
    val weight: String?,
    val photo: String?,
    val manuallyEdited: Boolean,
    val career: List<CoachCareerResponse>,   // gömülü kariyer
)
