package com.semihsahinoglu.sportseus.transfer.dto

data class TransferTeamsNode(
    val `in`: TransferTeamRefNode? = null,    // in — Kotlin keyword, backtick şart
    val out: TransferTeamRefNode? = null,
)
