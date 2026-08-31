package com.semihsahinoglu.sportseus.lineup.mapper

import com.semihsahinoglu.sportseus.fixture.entity.Fixture
import com.semihsahinoglu.sportseus.lineup.dto.LineupApiItem
import com.semihsahinoglu.sportseus.lineup.dto.LineupCoachSummary
import com.semihsahinoglu.sportseus.lineup.dto.LineupCreateRequest
import com.semihsahinoglu.sportseus.lineup.dto.LineupPlayerAddRequest
import com.semihsahinoglu.sportseus.lineup.dto.LineupPlayerInput
import com.semihsahinoglu.sportseus.lineup.dto.LineupPlayerItem
import com.semihsahinoglu.sportseus.lineup.dto.LineupPlayerNode
import com.semihsahinoglu.sportseus.lineup.dto.LineupResponse
import com.semihsahinoglu.sportseus.lineup.dto.LineupTeamSummary
import com.semihsahinoglu.sportseus.lineup.dto.LineupUpdateRequest
import com.semihsahinoglu.sportseus.lineup.entity.FixtureLineup
import com.semihsahinoglu.sportseus.lineup.entity.LineupPlayer
import com.semihsahinoglu.sportseus.team.entity.Team
import org.springframework.stereotype.Component

@Component
class LineupMapper {
    private val positionOrder = mapOf("G" to 0, "D" to 1, "M" to 2, "F" to 3)

    companion object {
        private val positionOrder = mapOf("G" to 0, "D" to 1, "M" to 2, "F" to 3)
    }

    fun toEntity(item: LineupApiItem, fixture: Fixture, team: Team): FixtureLineup =
        FixtureLineup(
            fixture = fixture,
            team = team,
            formation = item.formation,
            coachExternalId = item.coach?.id,
            coachName = item.coach?.name,
            coachPhoto = item.coach?.photo,
        )

    fun toEntity(fixture: Fixture, team: Team, request: LineupCreateRequest): FixtureLineup = FixtureLineup(
        fixture = fixture,
        team = team,
        formation = request.formation,
        coachExternalId = request.coachExternalId,
        coachName = request.coachName,
        coachPhoto = request.coachPhoto,
        manualAdded = true,
    )

    fun applyApiData(target: FixtureLineup, item: LineupApiItem) {
        target.formation = item.formation
        target.coachExternalId = item.coach?.id
        target.coachName = item.coach?.name
        target.coachPhoto = item.coach?.photo
    }

    fun toPlayerEntity(lineup: FixtureLineup, node: LineupPlayerNode, isStarter: Boolean): LineupPlayer =
        LineupPlayer(
            lineup = lineup,
            playerExternalId = requireNotNull(node.id) { "Lineup player id null olamaz" },
            playerName = node.name,
            number = node.number,
            position = node.pos,
            isStarter = isStarter,
        )

    fun toPlayerEntity(lineup: FixtureLineup, input: LineupPlayerInput, isStarter: Boolean): LineupPlayer =
        LineupPlayer(
            lineup = lineup,
            playerExternalId = input.playerExternalId,
            playerName = input.name,
            number = input.number,
            position = input.position,
            isStarter = isStarter,
        )

    fun toPlayerEntity(lineup: FixtureLineup, request: LineupPlayerAddRequest): LineupPlayer = LineupPlayer(
        lineup = lineup,
        playerExternalId = request.playerExternalId,
        playerName = request.name,
        number = request.number,
        position = request.position,
        isStarter = request.isStarter,
    )

    fun applyManualUpdate(target: FixtureLineup, request: LineupUpdateRequest) {
        request.formation?.let { target.formation = it }
        request.coachExternalId?.let { target.coachExternalId = it }
        request.coachName?.let { target.coachName = it }
        request.coachPhoto?.let { target.coachPhoto = it }
    }

    fun toResponse(lineup: FixtureLineup): LineupResponse =
        LineupResponse(
            id = lineup.id!!,
            formation = lineup.formation,
            manuallyEdited = lineup.manuallyEdited,
            team = LineupTeamSummary(
                id = lineup.team.id!!,
                externalId = lineup.team.externalId,
                name = lineup.team.name,
                logoUrl = lineup.team.logoUrl,
            ),
            coach = lineup.coachExternalId?.let { extId ->
                LineupCoachSummary(
                    externalId = extId,
                    name = lineup.coachName,
                    photo = lineup.coachPhoto,
                )
            },
            startXI = lineup.players.filter { it.isStarter }.map(::toPlayerItem),
            substitutes = lineup.players.filter { !it.isStarter }.sortedBy { positionRank(it.position) }
                .map(::toPlayerItem),
        )

    private fun toPlayerItem(p: LineupPlayer): LineupPlayerItem =
        LineupPlayerItem(
            id = p.id!!,
            playerExternalId = p.playerExternalId,
            name = p.playerName,
            number = p.number,
            position = p.position,
            isStarter = p.isStarter,
        )

    private fun positionRank(pos: String?): Int = positionOrder[pos?.uppercase()] ?: Int.MAX_VALUE
}