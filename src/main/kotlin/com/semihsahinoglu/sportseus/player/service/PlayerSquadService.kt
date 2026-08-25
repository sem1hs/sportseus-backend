package com.semihsahinoglu.sportseus.player.service

import com.semihsahinoglu.sportseus.player.dto.squad.SquadPlayerNode
import com.semihsahinoglu.sportseus.team.entity.Team
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PlayerSquadService(
    private val playerService: PlayerService,
    private val playerTeamService: PlayerTeamService,
) {

    @Transactional
    fun syncMember(team: Team, node: SquadPlayerNode, season: Int) {
        val player = playerService.ensureStub(node)          // REQUIRED → bu tx'e katılır

        playerTeamService.ensureMembership(
            // REQUIRED → aynı tx
            player = player,
            team = team,
            season = season,
            number = node.number,
            position = node.position,
        )
    }
}