package com.semihsahinoglu.sportseus.player.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerCreateRequest
import com.semihsahinoglu.sportseus.player.dto.PlayerResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerStatisticsResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerTeamHistoryResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerUpdateRequest
import com.semihsahinoglu.sportseus.player.dto.SquadResponse
import com.semihsahinoglu.sportseus.player.facade.PlayerFacade
import com.semihsahinoglu.sportseus.player.service.PlayerService
import com.semihsahinoglu.sportseus.player.service.PlayerStatisticsService
import com.semihsahinoglu.sportseus.player.service.PlayerTeamService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/admin/players")
class PlayerAdminController(
    private val playerFacade: PlayerFacade,
    private val playerService: PlayerService,
    private val playerTeamService: PlayerTeamService
) {
    // ADMIN: profil sync
    @PostMapping("/{externalId}/sync")
    fun syncProfile(@PathVariable externalId: Long): ResponseEntity<ApiResponse<PlayerResponse>> {
        val playerProfile = playerFacade.syncProfile(externalId)
        val response = ApiResponse.success(playerProfile)
        return ResponseEntity.ok(response)
    }

    // Kadro sync — POST /admin/players/squads/sync?teamId=998&season=2024
    @PostMapping("/squads/sync")
    fun syncSquad(
        @RequestParam teamId: Long,
        @RequestParam season: Int
    ): ResponseEntity<ApiResponse<SquadResponse>> {
        val playerSquad = playerFacade.syncSquad(teamId, season)
        val response = ApiResponse.success(playerSquad)
        return ResponseEntity.ok(response)
    }

    // Takım geçmişi sync — POST /admin/players/{externalId}/teams/sync
    @PostMapping("/{externalId}/teams/sync")
    fun syncTeamHistory(@PathVariable externalId: Long): ResponseEntity<ApiResponse<List<PlayerTeamHistoryResponse>>> {
        val playerTeamHistory = playerFacade.syncTeamHistory(externalId)
        val response = ApiResponse.success(playerTeamHistory)
        return ResponseEntity.ok(response)
    }

    // ADMIN: elle oyuncu ekle
    @PostMapping
    fun createPlayer(@RequestBody request: PlayerCreateRequest): ResponseEntity<ApiResponse<PlayerResponse>> {
        val player = playerService.create(request)
        val response = ApiResponse.success(player)
        return ResponseEntity.ok(response)
    }

    // Oyuncu silme - DELETE /admin/players/{id}
    @DeleteMapping("/{id}")
    fun deletePlayer(@PathVariable id: UUID): ResponseEntity<Void> {
        playerService.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    // Oyuncu Güncelleme - PATCH /admin/players/{id}
    @PatchMapping("/{id}")
    fun updatePlayer(
        @PathVariable id: UUID,
        @RequestBody request: PlayerUpdateRequest
    ): ResponseEntity<ApiResponse<PlayerResponse>> {
        val player = playerService.update(id, request)
        val response = ApiResponse.success(player)
        return ResponseEntity.ok(response)
    }

    // Oyuncu takım silme - DELETE /admin/players/teams/{id}
    @DeleteMapping("/teams/{id}")
    fun deletePlayerTeam(@PathVariable id: UUID): ResponseEntity<Void> {
        playerTeamService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}