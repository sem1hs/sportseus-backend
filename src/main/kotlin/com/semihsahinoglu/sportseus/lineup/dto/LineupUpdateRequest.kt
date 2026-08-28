package com.semihsahinoglu.sportseus.lineup.dto

data class LineupUpdateRequest(
    val formation: String? = null,
    val coachExternalId: Int? = null,
    val coachName: String? = null,
    val coachPhoto: String? = null,
)
