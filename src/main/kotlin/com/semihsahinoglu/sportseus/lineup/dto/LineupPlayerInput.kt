package com.semihsahinoglu.sportseus.lineup.dto

data class LineupPlayerInput(
    val playerExternalId: Int,
    val name: String? = null,
    val number: Int? = null,
    val position: String? = null,
)