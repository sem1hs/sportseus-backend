package com.semihsahinoglu.sportseus.coach.dto

import java.time.LocalDate

data class CoachBirthNode(
    val date: LocalDate? = null,            // "1952-06-01" — null olabilir (Tekke)
    val place: String? = null,
    val country: String? = null,
)