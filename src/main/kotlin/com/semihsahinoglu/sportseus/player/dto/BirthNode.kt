package com.semihsahinoglu.sportseus.player.dto

import java.time.LocalDate

data class BirthNode(
    val date: LocalDate? = null,    // "1992-06-15"
    val place: String? = null,
    val country: String? = null,
)