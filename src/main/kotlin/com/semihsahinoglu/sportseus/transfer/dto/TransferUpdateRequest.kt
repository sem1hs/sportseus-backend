package com.semihsahinoglu.sportseus.transfer.dto

import com.semihsahinoglu.sportseus.transfer.entity.TransferType
import java.time.LocalDate

data class TransferUpdateRequest(
    val date: LocalDate? = null,
    val rawType: String? = null,
    val transferType: TransferType? = null,
    val fee: Long? = null,
    val teamInExternalId: Int? = null,     // değişirse service Team'i çözer (katı)
    val teamOutExternalId: Int? = null,
)
