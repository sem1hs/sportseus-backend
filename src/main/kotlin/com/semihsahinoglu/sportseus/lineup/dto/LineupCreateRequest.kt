package com.semihsahinoglu.sportseus.lineup.dto

import java.util.UUID

data class LineupCreateRequest(
    val fixtureId: UUID,
    val teamExternalId: Int,
    val formation: String? = null,
    val coachExternalId: Int? = null,
    val coachName: String? = null,
    val coachPhoto: String? = null,
    val startXI: List<LineupPlayerInput> = emptyList(),
    val substitutes: List<LineupPlayerInput> = emptyList()
)
