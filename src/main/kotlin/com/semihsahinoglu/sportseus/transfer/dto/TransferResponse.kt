package com.semihsahinoglu.sportseus.transfer.dto

import com.semihsahinoglu.sportseus.transfer.entity.TransferType
import java.time.LocalDate
import java.util.UUID

data class TransferResponse(
    val id: UUID,                       // kaydın kimliği (silme/güncelleme için)
    val date: LocalDate,
    val rawType: String,                // "€ 42M" — kayıpsız orijinal
    val transferType: TransferType,     // SALE / LOAN / FREE / UNKNOWN
    val fee: Long?,                     // 42000000 (euro tam) — LOAN/FREE'de null
    val manuallyEdited: Boolean,        // elle düzenlendi mi
    val player: TransferPlayerSummary,
    val teamIn: TransferTeamSummary,
    val teamOut: TransferTeamSummary,
)
