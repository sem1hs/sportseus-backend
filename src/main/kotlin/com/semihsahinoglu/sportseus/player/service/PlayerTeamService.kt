package com.semihsahinoglu.sportseus.player.service

import com.semihsahinoglu.sportseus.player.dto.PlayerTeamHistoryResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerTeamTeamSummary
import com.semihsahinoglu.sportseus.player.dto.SquadResponse
import com.semihsahinoglu.sportseus.player.entity.Player
import com.semihsahinoglu.sportseus.player.entity.PlayerTeam
import com.semihsahinoglu.sportseus.player.mapper.PlayerTeamMapper
import com.semihsahinoglu.sportseus.player.mapper.SquadMapper
import com.semihsahinoglu.sportseus.player.repository.PlayerTeamRepository
import com.semihsahinoglu.sportseus.team.entity.Team
import com.semihsahinoglu.sportseus.team.exception.TeamNotFoundException
import com.semihsahinoglu.sportseus.team.service.TeamService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PlayerTeamService(
    private val teamService: TeamService,
    private val playerTeamRepository: PlayerTeamRepository,
    private val playerTeamMapper: PlayerTeamMapper,
    private val squadMapper: SquadMapper,
) {
    // ADMIN: ensurePlayerTeam, iki feeder'ın ortak helper'ı
    // Squad: number/position dolu gelir. /teams: ikisi de null gelir.
    // Kural: null gelen değerle mevcut dolu değeri EZME.
    @Transactional
    fun ensureMembership(
        player: Player,
        team: Team,
        season: Int,
        number: Int? = null,
        position: String? = null,
    ): PlayerTeam {
        val existing = playerTeamRepository.findByPlayerIdAndTeamIdAndSeason(player.id!!, team.id!!, season)

        val entity = if (existing != null) {
            if (number != null) existing.number = number       // sadece dolu gelince güncelle
            if (position != null) existing.position = position
            existing
        } else {
            PlayerTeam(
                player = player,
                team = team,
                season = season,
                number = number,
                position = position,
            )
        }

        return playerTeamRepository.save(entity)
    }

    // METHOD: takım listesi döner
    @Transactional(readOnly = true)
    fun getSquad(team: Team, season: Int): SquadResponse {
        val memberships = playerTeamRepository.findAllByTeamIdAndSeason(team.id!!, season)
        return squadMapper.toResponse(team, season, memberships)
    }

    @Transactional(readOnly = true)
    fun getSquadByTeamExternalId(teamExternalId: Long, season: Int): SquadResponse {
        val team = teamService.findByExternalIdOptional(teamExternalId.toInt())
            ?: throw TeamNotFoundException("Takım bulunamadı: team=$teamExternalId")
        return getSquad(team, season)              // mevcut Team-alan overload'a devret
    }

    // PUBLIC: read, bir oyuncunun tüm üyelik geçmişi
    @Transactional(readOnly = true)
    fun getTeamHistoryByPlayerExternalId(playerExternalId: Long): List<PlayerTeamHistoryResponse> {
        val memberships = playerTeamRepository.findAllByPlayerExternalIdOrderBySeasonDesc(playerExternalId)

        return memberships
            .groupBy { it.team.id!! }                       // takıma göre grupla
            .map { (_, rows) ->
                val team = rows.first().team
                PlayerTeamHistoryResponse(
                    team = PlayerTeamTeamSummary(
                        id = team.id!!,
                        externalId = team.externalId.toLong(),
                        name = team.name,
                        logoUrl = team.logoUrl,
                    ),
                    seasons = rows.map { it.season }.distinct().sortedDescending(),
                )
            }
            .sortedByDescending { it.seasons.first() }        // en güncel sezonu olan takım üstte
    }
}