package com.semihsahinoglu.sportseus.coach.dto

import java.time.LocalDate

data class CoachCareerNode(
    val team: CoachTeamNode? = null,        // snapshot'a açılacak (id/name/logo)
    val start: LocalDate? = null,           // "2024-09-01"
    val end: LocalDate? = null,             // null = hâlâ görevde (Tekke)
)