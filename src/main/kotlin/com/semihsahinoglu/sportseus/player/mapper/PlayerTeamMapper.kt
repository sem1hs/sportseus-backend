package com.semihsahinoglu.sportseus.player.mapper

import com.semihsahinoglu.sportseus.player.dto.PlayerTeamResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerTeamTeamSummary
import com.semihsahinoglu.sportseus.player.entity.Player
import com.semihsahinoglu.sportseus.player.entity.PlayerTeam
import com.semihsahinoglu.sportseus.team.entity.Team
import org.springframework.stereotype.Component

@Component
class PlayerTeamMapper {
    fun toResponse(pt: PlayerTeam): PlayerTeamResponse =
        PlayerTeamResponse(
            id = pt.id!!,
            season = pt.season,
            number = pt.number,
            position = pt.position,
            team = PlayerTeamTeamSummary(
                id = pt.team.id!!,
                externalId = pt.team.externalId.toLong(),
                name = pt.team.name,
                logoUrl = pt.team.logoUrl,
            ),
        )
}