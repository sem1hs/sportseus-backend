package com.semihsahinoglu.sportseus.lineup.dto

import java.util.UUID

data class LineupPlayerItem(
    val id: UUID,
    val playerExternalId: Int,
    val name: String?,
    val number: Int?,
    val position: String?,
    val isStarter: Boolean?,
)
