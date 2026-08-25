package com.semihsahinoglu.sportseus.player.dto

data class PlayerTeamHistoryResponse(
    val team: PlayerTeamTeamSummary,   // zaten var (id, externalId, name, logoUrl)
    val seasons: List<Int>
)
