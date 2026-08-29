package com.semihsahinoglu.sportseus.standing.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.standing.dto.StandingResponse
import com.semihsahinoglu.sportseus.standing.dto.StandingUpdateRequest
import com.semihsahinoglu.sportseus.standing.service.StandingService
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
@RequestMapping("/admin/standings")
class StandingAdminController(
    private val standingService: StandingService
) {
    // ADMIN: ligin puan tablosunu sync
    @PostMapping("/sync")
    fun sync(
        @RequestParam leagueId: Int,
        @RequestParam season: Int,
    ): ResponseEntity<ApiResponse<List<StandingResponse>>> {
        val standing = standingService.sync(leagueId, season)
        val response = ApiResponse.success(standing)
        return ResponseEntity.ok(response)
    }

    // ADMIN: elle güncelleme (partial)
    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody request: StandingUpdateRequest,
    ): ResponseEntity<ApiResponse<StandingResponse>> {
        val standing = standingService.update(id, request)
        val response = ApiResponse.success(standing)
        return ResponseEntity.ok(response)
    }

    // ADMIN: tekil silme
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        standingService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}