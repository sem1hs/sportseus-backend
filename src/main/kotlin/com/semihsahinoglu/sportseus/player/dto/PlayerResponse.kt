package com.semihsahinoglu.sportseus.player.dto

import java.time.LocalDate
import java.util.UUID

data class PlayerResponse(
    val id: UUID,
    val externalId: Long,
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
)