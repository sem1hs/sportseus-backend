package com.semihsahinoglu.sportseus.transfer.dto

data class TransferTeamRefNode(
    val id: Long? = null,      // null olabilir (çöp veri — "Free agent" satırı)
    val name: String? = null,
    val logo: String? = null,
)
