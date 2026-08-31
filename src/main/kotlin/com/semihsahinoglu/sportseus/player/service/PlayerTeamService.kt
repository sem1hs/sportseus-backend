package com.semihsahinoglu.sportseus.player.service

import com.semihsahinoglu.sportseus.player.dto.PlayerTeamCreateRequest
import com.semihsahinoglu.sportseus.player.dto.PlayerTeamResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerTeamUpdateRequest
import com.semihsahinoglu.sportseus.squad.dto.SquadResponse
import com.semihsahinoglu.sportseus.player.entity.Player
import com.semihsahinoglu.sportseus.player.entity.PlayerTeam
import com.semihsahinoglu.sportseus.player.exception.PlayerTeamConflictException
import com.semihsahinoglu.sportseus.player.exception.PlayerTeamNotFoundException
import com.semihsahinoglu.sportseus.player.mapper.PlayerTeamMapper
import com.semihsahinoglu.sportseus.squad.mapper.SquadMapper
import com.semihsahinoglu.sportseus.player.repository.PlayerTeamRepository
import com.semihsahinoglu.sportseus.team.entity.Team
import com.semihsahinoglu.sportseus.team.exception.TeamNotFoundException
import com.semihsahinoglu.sportseus.team.service.TeamService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class PlayerTeamService(
    private val teamService: TeamService,
    private val playerService: PlayerService,
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
            if (existing.manualAdded || existing.manuallyEdited) return existing   // elle → dokunma
            if (number != null) existing.number = number
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

    // ADMIN: elle üyelik ekle (playerId UUID + teamExternalId, çakışma 409)
    @Transactional
    fun create(request: PlayerTeamCreateRequest): PlayerTeamResponse {
        val player = playerService.findById(request.playerId)
        val team = teamService.findByExternalIdOptional(request.teamExternalId)
            ?: throw TeamNotFoundException("Takım bulunamadı: team=${request.teamExternalId}")

        // composite çakışma
        val exists = playerTeamRepository
            .findByPlayerIdAndTeamIdAndSeason(player.id!!, team.id!!, request.season) != null

        if (exists) throw PlayerTeamConflictException("Bu üyelik zaten var: player=${request.playerId} team=${request.teamExternalId} season=${request.season}")

        val playerTeam = playerTeamMapper.toEntity(player, team, request)
        val saved = playerTeamRepository.save(playerTeam)
        return playerTeamMapper.toResponse(saved)
    }

    // ADMIN: elle güncelleme (UUID, FK değişimi katı + composite çakışma)
    @Transactional
    fun update(id: UUID, request: PlayerTeamUpdateRequest): PlayerTeamResponse {
        val membership =
            playerTeamRepository.findById(id).orElseThrow { PlayerTeamNotFoundException("Üyelik bulunamadı: id=$id") }

        request.playerId?.let { pid ->
            val player = playerService.findById(pid)
            membership.addPlayer(player)
        }
        request.teamExternalId?.let { extId ->
            val team = teamService.findByExternalIdOptional(extId)
                ?: throw TeamNotFoundException("Takım bulunamadı: team=$extId")
            membership.addTeam(team)
        }

        membership.updateEntity(request.season, request.number, request.position)

        return try {
            playerTeamMapper.toResponse(playerTeamRepository.saveAndFlush(membership))
        } catch (e: DataIntegrityViolationException) {
            throw PlayerTeamConflictException(
                "Bu güncelleme mevcut bir üyelikle çakışıyor (aynı player+team+season): id=$id"
            )
        }
    }

    // ADMIN: player team siler
    @Transactional
    fun deleteById(id: UUID) {
        if (!playerTeamRepository.existsById(id)) throw PlayerTeamNotFoundException("Üyelik bulunamadı: id=$id")
        playerTeamRepository.deleteById(id)
    }

    // PUBLIC: read, bir oyuncunun tüm üyelikleri (düz, her satır ayrı)
    @Transactional(readOnly = true)
    fun getPlayerTeamByPlayerExternalId(playerExternalId: Long): List<PlayerTeamResponse> =
        playerTeamRepository.findAllByPlayerExternalIdOrderBySeasonDesc(playerExternalId)
            .map(playerTeamMapper::toResponse)

    // PUBLIC: read, bir oyuncunun tüm üyelikleri (düz, UUID ile)
    @Transactional(readOnly = true)
    fun getPlayerTeamByPlayerId(playerId: UUID): List<PlayerTeamResponse> =
        playerTeamRepository.findAllByPlayerIdOrderBySeasonDesc(playerId)
            .map(playerTeamMapper::toResponse)

    // METHOD: takım idsine göre squad bulma
    fun findAllByTeamIdAndSeason(team: Team, season: Int): List<PlayerTeam> =
        playerTeamRepository.findAllByTeamIdAndSeason(team.id!!, season)
}