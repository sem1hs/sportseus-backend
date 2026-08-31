package com.semihsahinoglu.sportseus.transfer.dto

import com.semihsahinoglu.sportseus.transfer.entity.TransferType
import java.time.LocalDate
import java.util.UUID

data class TransferCreateRequest(
    val playerId: UUID,
    val teamInExternalId: Int,
    val teamOutExternalId: Int,
    val date: LocalDate,
    val rawType: String,
    val transferType: TransferType,
    val fee: Long? = null,
)
