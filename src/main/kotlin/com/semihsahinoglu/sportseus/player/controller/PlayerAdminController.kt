package com.semihsahinoglu.sportseus.player.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerStatisticsResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerTeamHistoryResponse
import com.semihsahinoglu.sportseus.player.dto.SquadResponse
import com.semihsahinoglu.sportseus.player.facade.PlayerFacade
import com.semihsahinoglu.sportseus.player.service.PlayerService
import com.semihsahinoglu.sportseus.player.service.PlayerStatisticsService
import com.semihsahinoglu.sportseus.player.service.PlayerTeamService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/admin/players")
class PlayerAdminController(
    private val playerFacade: PlayerFacade,
    private val playerStatisticsService: PlayerStatisticsService,
    private val playerService: PlayerService,
    private val playerTeamService: PlayerTeamService
) {
    // 1) Profil sync — POST /admin/players/{externalId}/sync
    @PostMapping("/{externalId}/sync")
    fun syncProfile(@PathVariable externalId: Long): ResponseEntity<ApiResponse<PlayerResponse>> {
        val playerProfile = playerFacade.syncProfile(externalId)
        val response = ApiResponse.success(playerProfile)
        return ResponseEntity.ok(response)
    }

    // 2) İstatistik sync — POST /admin/players/{externalId}/statistics/sync?season=2024
    @PostMapping("/{externalId}/statistics/sync")
    fun syncStatistics(
        @PathVariable externalId: Long,
        @RequestParam season: Int
    ): ResponseEntity<ApiResponse<List<PlayerStatisticsResponse>>> {
        val playerStatistics = playerFacade.syncStatistics(externalId, season)
        val response = ApiResponse.success(playerStatistics)
        return ResponseEntity.ok(response)
    }

    // 3) Kadro sync — POST /admin/players/squads/sync?teamId=998&season=2024
    @PostMapping("/squads/sync")
    fun syncSquad(
        @RequestParam teamId: Long,
        @RequestParam season: Int
    ): ResponseEntity<ApiResponse<SquadResponse>> {
        val playerSquad = playerFacade.syncSquad(teamId, season)
        val response = ApiResponse.success(playerSquad)
        return ResponseEntity.ok(response)
    }

    // 4) Takım geçmişi sync — POST /admin/players/{externalId}/teams/sync
    @PostMapping("/{externalId}/teams/sync")
    fun syncTeamHistory(@PathVariable externalId: Long): ResponseEntity<ApiResponse<List<PlayerTeamHistoryResponse>>> {
        val playerTeamHistory = playerFacade.syncTeamHistory(externalId)
        val response = ApiResponse.success(playerTeamHistory)
        return ResponseEntity.ok(response)
    }

    // 5) İstatistik silme — DELETE /admin/players/statistics/{id}
    @DeleteMapping("/statistics/{id}")
    fun deleteStatistics(@PathVariable id: UUID): ResponseEntity<Void> {
        playerStatisticsService.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    // 6) Oyuncu silme - DELETE /admin/players/{externalId}
    @DeleteMapping("/{externalId}")
    fun deletePlayer(@PathVariable externalId: Long): ResponseEntity<Void> {
        playerService.deleteByExternalId(externalId)
        return ResponseEntity.noContent().build()
    }

    // 7) Oyuncu takım silme - DELETE /admin/players/teams/{id}
    @DeleteMapping("/teams/{id}")
    fun deletePlayerTeam(@PathVariable id: UUID): ResponseEntity<Void> {
        playerTeamService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}