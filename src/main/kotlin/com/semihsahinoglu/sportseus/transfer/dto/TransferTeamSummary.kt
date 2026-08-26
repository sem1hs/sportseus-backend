package com.semihsahinoglu.sportseus.transfer.dto

import java.util.UUID

data class TransferTeamSummary(
    val id: UUID,
    val externalId: Long,
    val name: String,
    val logoUrl: String?,
)
