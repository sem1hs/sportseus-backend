package com.semihsahinoglu.sportseus.player.dto

import java.time.LocalDate

data class PlayerUpdateRequest(
    val name: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val age: Int? = null,
    val birthDate: LocalDate? = null,
    val birthPlace: String? = null,
    val birthCountry: String? = null,
    val nationality: String? = null,
    val height: String? = null,
    val weight: String? = null,
    val photo: String? = null
)
