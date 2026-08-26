package com.semihsahinoglu.sportseus.transfer.dto

import java.util.UUID

data class TransferPlayerSummary(
    val id: UUID,
    val externalId: Long,
    val name: String,
    val photo: String?,
)