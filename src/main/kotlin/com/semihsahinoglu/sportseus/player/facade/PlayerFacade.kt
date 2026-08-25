package com.semihsahinoglu.sportseus.player.facade

import com.semihsahinoglu.sportseus.league.entity.League
import com.semihsahinoglu.sportseus.league.service.LeagueService
import com.semihsahinoglu.sportseus.player.client.PlayerApiClient
import com.semihsahinoglu.sportseus.player.dto.PlayerResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerStatisticsResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerTeamHistoryResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerTeamResponse
import com.semihsahinoglu.sportseus.player.dto.SquadResponse
import com.semihsahinoglu.sportseus.player.exception.MissingReferencesException
import com.semihsahinoglu.sportseus.player.exception.PlayerNotFoundException
import com.semihsahinoglu.sportseus.player.service.PlayerService
import com.semihsahinoglu.sportseus.player.service.PlayerSquadService
import com.semihsahinoglu.sportseus.player.service.PlayerStatisticsService
import com.semihsahinoglu.sportseus.player.service.PlayerTeamService
import com.semihsahinoglu.sportseus.team.entity.Team
import com.semihsahinoglu.sportseus.team.service.TeamService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PlayerFacade(
    private val playerApiClient: PlayerApiClient,
    private val playerService: PlayerService,
    private val playerSquadService: PlayerSquadService,
    private val playerStatisticsService: PlayerStatisticsService,
    private val playerTeamService: PlayerTeamService,
    private val teamService: TeamService,
    private val leagueService: LeagueService,
) {
    private val log = LoggerFactory.getLogger(PlayerFacade::class.java)

    // ── 1) PROFİL ───────────────────────────────────────────
    fun syncProfile(playerExternalId: Long): PlayerResponse =
        playerService.syncProfile(playerExternalId)

    // ── 2) İSTATİSTİK ───────────────────────────────────────
    fun syncStatistics(playerExternalId: Long, season: Int): List<PlayerStatisticsResponse> {
        val apiItem = playerApiClient.fetchPlayerWithStats(playerExternalId, season)
            ?: throw PlayerNotFoundException("API-Football'da oyuncu/istatistik yok: player=$playerExternalId season=$season")
        val playerNode =
            apiItem.player ?: throw PlayerNotFoundException("API yanıtında oyuncu bloğu yok: player=$playerExternalId")

        // sadece team+league id'si dolu item'lar
        val items = apiItem.statistics.filter { it.team?.id != null && it.league?.id != null }

        // player upsert — tek sefer (hiç yazılabilir item olmasa bile profil güncellensin)
        val player = playerService.upsertFromNode(playerNode)

        // her item: team+league çözülebiliyorsa yaz, biri bile yoksa ATLA
        val responses = items.mapNotNull { item ->
            val teamExtId = item.team!!.id!!
            val leagueExtId = item.league!!.id!!

            val team = teamService.findByExternalId(teamExtId.toInt())
            val league = leagueService.findByExternalIdAndSeasonEntity(leagueExtId.toInt(), season)

            if (team == null || league == null) return@mapNotNull null

            playerStatisticsService.upsert(player, team, league, season, item)
        }

        return responses
    }

    // ── 3) KADRO ────────────────────────────────────────────
    fun syncSquad(teamExternalId: Long, season: Int): SquadResponse {
        // tek takım — anında çöz, yoksa hemen patlat (toplu rapora gerek yok)
        val team = teamService.findByExternalIdOptional(teamExternalId.toInt()) ?: throw MissingReferencesException(
            listOf(teamExternalId), emptyList()
        )

        val squad = playerApiClient.fetchSquad(teamExternalId)
            ?: throw PlayerNotFoundException("API-Football'da kadro bulunamadı: team=$teamExternalId")

        squad.players.forEach { node ->
            try {
                playerSquadService.syncMember(team, node, season)
            } catch (e: Exception) {
                log.warn("Kadro oyuncusu atlandı: player={} sebep={}", node.id, e.message)
            }
        }

        return playerTeamService.getSquad(team, season)
    }

    // ── 4) TAKIM GEÇMİŞİ ────────────────────────────────────
    fun syncTeamHistory(playerExternalId: Long): List<PlayerTeamHistoryResponse> {
        // /teams profil taşımaz → player önceden var olmalı
        val player = playerService.getByExternalIdOrThrow(playerExternalId)
        val history = playerApiClient.fetchTeamHistory(playerExternalId)

        // (team, season) düzleştir; boş seasons'ı (Fiorentina) atla
        val pairs = history.flatMap { entry ->
            val teamId = entry.team?.id ?: return@flatMap emptyList()
            entry.seasons.map { season -> teamId to season }
        }

        // KATI FK — toplu: tüm takımlar var mı?
        val teamMap = HashMap<Long, Team>()
        val missingTeams = ArrayList<Long>()
        pairs.map { it.first }.toSet().forEach { id ->
            teamService.findByExternalId(id.toInt())?.let { teamMap[id] = it } ?: missingTeams.add(id)
        }

        if (missingTeams.isNotEmpty())
            throw MissingReferencesException(missingTeams, emptyList())

        // her (team, season) → iskelet üyelik (number/position YOK; squad doldurur)
        pairs.forEach { (teamExternalId, season) ->
            playerTeamService.ensureMembership(player, teamMap.getValue(teamExternalId), season)
        }

        return playerTeamService.getTeamHistoryByPlayerExternalId(playerExternalId)
    }
}