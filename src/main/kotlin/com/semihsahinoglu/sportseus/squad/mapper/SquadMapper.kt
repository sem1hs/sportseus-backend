package com.semihsahinoglu.sportseus.squad.mapper

import com.semihsahinoglu.sportseus.squad.dto.SquadPlayerItem
import com.semihsahinoglu.sportseus.squad.dto.SquadResponse
import com.semihsahinoglu.sportseus.squad.dto.SquadTeamSummary
import com.semihsahinoglu.sportseus.player.entity.PlayerTeam
import com.semihsahinoglu.sportseus.team.entity.Team
import org.springframework.stereotype.Component

@Component
class SquadMapper {

    // Bir takımın bir sezondaki üyelik listesini squad response'a montajlar.
    // memberships boşsa team bilgisini kaybetmemek için team ayrı geçilir.
    fun toResponse(team: Team, season: Int, memberships: List<PlayerTeam>): SquadResponse =
        SquadResponse(
            season = season,
            team = SquadTeamSummary(
                id = team.id!!,
                externalId = team.externalId.toLong(),
                name = team.name,
                logoUrl = team.logoUrl,
            ),
            players = memberships.map(::toPlayerItem),
        )

    private fun toPlayerItem(pt: PlayerTeam): SquadPlayerItem =
        SquadPlayerItem(
            playerTeamId = pt.id!!,
            id = pt.player.id!!,
            externalId = pt.player.externalId,
            name = pt.player.name,
            birthDate = pt.player.birthDate,
            photo = pt.player.photo,
            number = pt.number,
            position = pt.position,
            nationality = pt.player.nationality,
        )
}