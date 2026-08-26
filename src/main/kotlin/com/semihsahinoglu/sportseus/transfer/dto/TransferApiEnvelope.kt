package com.semihsahinoglu.sportseus.transfer.dto


data class TransferApiEnvelope(
    val response: List<TransferApiItem> = emptyList(),
)