package com.semihsahinoglu.sportseus.player.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerTeamCreateRequest
import com.semihsahinoglu.sportseus.player.dto.PlayerTeamResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerTeamUpdateRequest
import com.semihsahinoglu.sportseus.player.facade.PlayerFacade
import com.semihsahinoglu.sportseus.player.service.PlayerTeamService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/admin/players/teams")
class PlayerTeamAdminController(
    private val playerFacade: PlayerFacade,
    private val playerTeamService: PlayerTeamService
) {

    // ADMIN: takım geçmişi sync
    @PostMapping("/sync/{externalId}")
    fun syncTeamHistory(@PathVariable externalId: Long): ResponseEntity<ApiResponse<List<PlayerTeamResponse>>> {
        val playerTeam = playerFacade.syncTeam(externalId)
        val response = ApiResponse.success(playerTeam)
        return ResponseEntity.ok(response)
    }

    // ADMIN: elle player team oluşturma
    @PostMapping
    fun createPlayerTeam(@RequestBody request: PlayerTeamCreateRequest): ResponseEntity<ApiResponse<PlayerTeamResponse>> {
        val playerTeam = playerTeamService.create(request)
        val response = ApiResponse.success(playerTeam)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    // ADMIN: elle player team güncelleme
    @PatchMapping("/{id}")
    fun updatePlayerTeam(
        @PathVariable id: UUID,
        @RequestBody request: PlayerTeamUpdateRequest,
    ): ResponseEntity<ApiResponse<PlayerTeamResponse>> {
        val playerTeam = playerTeamService.update(id, request)
        val response = ApiResponse.success(playerTeam)
        return ResponseEntity.ok(response)
    }

    // ADMIN: player team silme
    @DeleteMapping("/{id}")
    fun deletePlayerTeam(@PathVariable id: UUID): ResponseEntity<Void> {
        playerTeamService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}