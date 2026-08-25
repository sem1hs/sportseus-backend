package com.semihsahinoglu.sportseus.player.dto.squad

import com.semihsahinoglu.sportseus.player.dto.TeamRefNode

data class SquadApiItem(
    val team: TeamRefNode? = null,
    val players: List<SquadPlayerNode> = emptyList(),
)