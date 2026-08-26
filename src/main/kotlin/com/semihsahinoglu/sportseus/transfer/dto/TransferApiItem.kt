package com.semihsahinoglu.sportseus.transfer.dto

data class TransferApiItem(
    val player: TransferPlayerNode? = null,
    val transfers: List<TransferNode> = emptyList(),
)
