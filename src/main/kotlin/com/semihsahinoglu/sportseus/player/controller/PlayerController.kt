package com.semihsahinoglu.sportseus.player.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerStatisticsResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerTeamHistoryResponse
import com.semihsahinoglu.sportseus.player.dto.SquadResponse
import com.semihsahinoglu.sportseus.player.service.PlayerService
import com.semihsahinoglu.sportseus.player.service.PlayerStatisticsService
import com.semihsahinoglu.sportseus.player.service.PlayerTeamService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/players")
class PlayerController(
    private val playerService: PlayerService,
    private val playerStatisticsService: PlayerStatisticsService,
    private val playerTeamService: PlayerTeamService,
) {

    // Profil — GET /players/{externalId}
    @GetMapping("/{externalId}")
    fun getProfile(@PathVariable externalId: Long): ResponseEntity<ApiResponse<PlayerResponse>> {
        val playerProfile = playerService.getByExternalId(externalId)
        val response = ApiResponse.success(playerProfile)
        return ResponseEntity.ok(response)
    }

    // Sezon istatistikleri — GET /players/{externalId}/statistics?season=2024
    @GetMapping("/{externalId}/statistics")
    fun getStatistics(
        @PathVariable externalId: Long,
        @RequestParam season: Int,
    ): ResponseEntity<ApiResponse<List<PlayerStatisticsResponse>>> {
        val playerStatistics = playerStatisticsService.getByPlayerAndSeason(externalId, season)
        val response = ApiResponse.success(playerStatistics)
        return ResponseEntity.ok(response)
    }

    // Takım geçmişi — GET /players/{externalId}/teams
    @GetMapping("/{externalId}/teams")
    fun getTeamHistory(@PathVariable externalId: Long): ResponseEntity<ApiResponse<List<PlayerTeamHistoryResponse>>> {
        val playerTeamHistory = playerTeamService.getTeamHistoryByPlayerExternalId(externalId)
        val response = ApiResponse.success(playerTeamHistory)
        return ResponseEntity.ok(response)
    }

    // Kadro — GET /players/squads?team={teamExternalId}&season=2024
    @GetMapping("/squads")
    fun getSquad(
        @RequestParam team: Long,
        @RequestParam season: Int
    ): ResponseEntity<ApiResponse<SquadResponse>> {
        val squad = playerTeamService.getSquadByTeamExternalId(team, season)
        val response = ApiResponse.success(squad)
        return ResponseEntity.ok(response)
    }
}