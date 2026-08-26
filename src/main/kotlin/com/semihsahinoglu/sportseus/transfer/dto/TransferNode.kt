package com.semihsahinoglu.sportseus.transfer.dto

data class TransferNode(
    val date: java.time.LocalDate? = null,   // "2017-07-01"
    val type: String? = null,                // "€ 42M" / "Loan" / "Free agent"
    val teams: TransferTeamsNode? = null,
)
