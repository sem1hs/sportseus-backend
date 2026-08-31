package com.semihsahinoglu.sportseus.player.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerStatisticsCreateRequest
import com.semihsahinoglu.sportseus.player.dto.PlayerStatisticsResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerStatisticsUpdateRequest
import com.semihsahinoglu.sportseus.player.facade.PlayerFacade
import com.semihsahinoglu.sportseus.player.service.PlayerStatisticsService
import org.springframework.http.HttpStatus
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
@RequestMapping("/admin/players/statistics")
class PlayerStatisticsAdminController(
    private val playerStatisticsService: PlayerStatisticsService,
    private val playerFacade: PlayerFacade,
) {
    // ADMIN: istatistik sync
    @PostMapping("/sync/{externalId}")
    fun syncStatistics(
        @PathVariable externalId: Long,
        @RequestParam season: Int
    ): ResponseEntity<ApiResponse<List<PlayerStatisticsResponse>>> {
        val playerStatistics = playerFacade.syncStatistics(externalId, season)
        val response = ApiResponse.success(playerStatistics)
        return ResponseEntity.ok(response)
    }

    // ADMIN: elle istatistik ekle
    @PostMapping
    fun createStatistics(@RequestBody request: PlayerStatisticsCreateRequest): ResponseEntity<ApiResponse<PlayerStatisticsResponse>> {
        val playerStatistics = playerStatisticsService.create(request)
        val response = ApiResponse.success(playerStatistics)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    // ADMIN: istatistik güncelle
    @PatchMapping("/{id}")
    fun updateStatistics(
        @PathVariable id: UUID,
        @RequestBody request: PlayerStatisticsUpdateRequest,
    ): ResponseEntity<ApiResponse<PlayerStatisticsResponse>> {
        val playerStatistics = playerStatisticsService.update(id, request)
        val response = ApiResponse.success(playerStatistics)
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    // ADMIn: istatistik silme
    @DeleteMapping("/{id}")
    fun deleteStatistics(@PathVariable id: UUID): ResponseEntity<Void> {
        playerStatisticsService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}