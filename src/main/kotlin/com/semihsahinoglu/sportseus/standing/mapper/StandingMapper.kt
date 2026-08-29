package com.semihsahinoglu.sportseus.standing.mapper

import com.semihsahinoglu.sportseus.league.entity.League
import com.semihsahinoglu.sportseus.standing.dto.StandingResponse
import com.semihsahinoglu.sportseus.standing.dto.StandingRowNode
import com.semihsahinoglu.sportseus.standing.dto.StandingStatsDto
import com.semihsahinoglu.sportseus.standing.dto.StandingStatsNode
import com.semihsahinoglu.sportseus.standing.dto.StandingTeamSummary
import com.semihsahinoglu.sportseus.standing.entity.Standing
import com.semihsahinoglu.sportseus.standing.entity.StandingStats
import com.semihsahinoglu.sportseus.team.entity.Team
import org.springframework.stereotype.Component

@Component
class StandingMapper {

    fun toEntity(node: StandingRowNode, league: League, team: Team, season: Int): Standing =
        Standing(
            league = league,
            team = team,
            season = season,
            rank = node.rank,
            points = node.points,
            goalsDiff = node.goalsDiff,
            group = node.group,
            form = node.form,
            status = node.status,
            description = node.description,
            all = node.all.toStats(),
            home = node.home.toStats(),
            away = node.away.toStats(),
        )

    fun applyApiData(target: Standing, node: StandingRowNode) {
        target.rank = node.rank
        target.points = node.points
        target.goalsDiff = node.goalsDiff
        target.group = node.group
        target.form = node.form
        target.status = node.status
        target.description = node.description
        target.all = node.all.toStats()
        target.home = node.home.toStats()
        target.away = node.away.toStats()
    }

    fun toResponse(s: Standing): StandingResponse =
        StandingResponse(
            id = s.id!!,
            season = s.season,
            rank = s.rank,
            points = s.points,
            goalsDiff = s.goalsDiff,
            group = s.group,
            form = s.form,
            status = s.status,
            description = s.description,
            team = StandingTeamSummary(
                id = s.team.id!!,
                externalId = s.team.externalId,
                name = s.team.name,
                logoUrl = s.team.logoUrl,
            ),
            all = s.all.toDto(),
            home = s.home.toDto(),
            away = s.away.toDto(),
        )

    private fun StandingStatsNode?.toStats(): StandingStats =
        StandingStats(
            played = this?.played,
            win = this?.win,
            draw = this?.draw,
            lose = this?.lose,
            goalsFor = this?.goals?.`for`,          // iç içe → düz
            goalsAgainst = this?.goals?.against,
        )

    private fun StandingStats?.toDto(): StandingStatsDto =
        StandingStatsDto(
            played = this?.played,
            win = this?.win,
            draw = this?.draw,
            lose = this?.lose,
            goalsFor = this?.goalsFor,
            goalsAgainst = this?.goalsAgainst,
        )
}