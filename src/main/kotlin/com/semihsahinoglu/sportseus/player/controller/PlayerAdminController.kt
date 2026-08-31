package com.semihsahinoglu.sportseus.player.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerCreateRequest
import com.semihsahinoglu.sportseus.player.dto.PlayerResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerUpdateRequest
import com.semihsahinoglu.sportseus.player.facade.PlayerFacade
import com.semihsahinoglu.sportseus.player.service.PlayerService
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
@RequestMapping("/admin/players")
class PlayerAdminController(
    private val playerFacade: PlayerFacade,
    private val playerService: PlayerService,
) {
    // ADMIN: profil sync
    @PostMapping("/{externalId}/sync")
    fun syncProfile(@PathVariable externalId: Long): ResponseEntity<ApiResponse<PlayerResponse>> {
        val playerProfile = playerFacade.syncProfile(externalId)
        val response = ApiResponse.success(playerProfile)
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
}