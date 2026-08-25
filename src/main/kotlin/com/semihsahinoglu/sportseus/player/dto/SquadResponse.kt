package com.semihsahinoglu.sportseus.player.dto

data class SquadResponse(
    val season: Int,
    val team: SquadTeamSummary,
    val players: List<SquadPlayerItem>,
)
