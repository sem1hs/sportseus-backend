package com.semihsahinoglu.sportseus.player.mapper

import com.semihsahinoglu.sportseus.league.entity.League
import com.semihsahinoglu.sportseus.player.dto.PlayerStatisticsResponse
import com.semihsahinoglu.sportseus.player.dto.StatLeagueSummary
import com.semihsahinoglu.sportseus.player.dto.StatTeamSummary
import com.semihsahinoglu.sportseus.player.dto.statistic.PlayerStatItem
import com.semihsahinoglu.sportseus.player.dto.statistic.PlayerStatisticsNode
import com.semihsahinoglu.sportseus.player.entity.Player
import com.semihsahinoglu.sportseus.player.entity.PlayerStatistics
import com.semihsahinoglu.sportseus.team.entity.Team
import org.springframework.stereotype.Component

@Component
class PlayerStatisticsMapper {
    fun toNode(item: PlayerStatItem): PlayerStatisticsNode =
        PlayerStatisticsNode(games = item.games, goals = item.goals)

    fun toEntity(player: Player, team: Team, league: League, season: Int, stats: String): PlayerStatistics =
        PlayerStatistics(player = player, team = team, league = league, season = season, stats = stats)

    fun toManuelEntity(player: Player, team: Team, league: League, season: Int, stats: String): PlayerStatistics =
        PlayerStatistics(
            player = player,
            team = team,
            league = league,
            season = season,
            stats = stats,
            manualAdded = true,
            manuallyEdited = false,
        )

    fun toResponse(entity: PlayerStatistics, node: PlayerStatisticsNode): PlayerStatisticsResponse =
        PlayerStatisticsResponse(
            id = entity.id!!,
            season = entity.season,
            team = StatTeamSummary(
                id = entity.team.id!!,
                externalId = entity.team.externalId.toLong(),
                name = entity.team.name,
                logoUrl = entity.team.logoUrl,
            ),
            league = StatLeagueSummary(
                id = entity.league.id!!,
                externalId = entity.league.externalId.toLong(),
                name = entity.league.name,
                logoUrl = entity.league.logoUrl,
            ),
            statistics = node,
        )
}