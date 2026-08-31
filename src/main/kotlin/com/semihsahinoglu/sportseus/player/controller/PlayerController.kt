package com.semihsahinoglu.sportseus.player.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerStatisticsResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerTeamResponse
import com.semihsahinoglu.sportseus.squad.dto.SquadResponse
import com.semihsahinoglu.sportseus.player.service.PlayerService
import com.semihsahinoglu.sportseus.player.service.PlayerStatisticsService
import com.semihsahinoglu.sportseus.player.service.PlayerTeamService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/players")
class PlayerController(
    private val playerService: PlayerService,
    private val playerStatisticsService: PlayerStatisticsService,
    private val playerTeamService: PlayerTeamService,
) {

    // PUBLIC: profil
    @GetMapping("/{id}")
    fun getProfile(@PathVariable id: UUID): ResponseEntity<ApiResponse<PlayerResponse>> {
        val playerProfile = playerService.getById(id)
        val response = ApiResponse.success(playerProfile)
        return ResponseEntity.ok(response)
    }

    // PUBLIC: sezon istatistikleri
    @GetMapping("/{id}/statistics")
    fun getStatistics(
        @PathVariable id: UUID,
        @RequestParam season: Int,
    ): ResponseEntity<ApiResponse<List<PlayerStatisticsResponse>>> {
        val playerStatistics = playerStatisticsService.getByPlayerAndSeason(id, season)
        val response = ApiResponse.success(playerStatistics)
        return ResponseEntity.ok(response)
    }

    // PUBLIC: takım geçmişi
    @GetMapping("/{id}/teams")
    fun getTeamHistory(@PathVariable id: UUID): ResponseEntity<ApiResponse<List<PlayerTeamResponse>>> {
        val playerTeamHistory = playerTeamService.getPlayerTeamByPlayerId(id)
        val response = ApiResponse.success(playerTeamHistory)
        return ResponseEntity.ok(response)
    }
}