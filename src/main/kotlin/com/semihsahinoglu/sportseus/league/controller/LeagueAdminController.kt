package com.semihsahinoglu.sportseus.league.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.league.dto.LeagueResponse
import com.semihsahinoglu.sportseus.league.dto.LeagueUpdateRequest
import com.semihsahinoglu.sportseus.league.service.LeagueService
import jakarta.validation.Valid
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
@RequestMapping("/admin/leagues")
class LeagueAdminController(
    private val leagueService: LeagueService
) {
    // API-Football'dan çekip kaydet
    @PostMapping("/{externalId}")
    fun importLeague(
        @PathVariable externalId: Int, @RequestParam season: Int,
    ): ResponseEntity<ApiResponse<LeagueResponse>> {
        val league = leagueService.importLeague(externalId, season)
        val response = ApiResponse.success(league)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    // Elle partial güncelleme
    @PatchMapping("/{id}")
    fun updateLeague(
        @PathVariable id: UUID, @Valid @RequestBody request: LeagueUpdateRequest,
    ): ResponseEntity<ApiResponse<LeagueResponse>> {
        val league = leagueService.updateLeague(id, request)
        val response = ApiResponse.success(league)
        return ResponseEntity.ok(response)
    }

    // Hard delete
    @DeleteMapping("/{id}")
    fun deleteLeague(@PathVariable id: UUID): ResponseEntity<Void> {
        leagueService.deleteLeague(id)
        return ResponseEntity.noContent().build()
    }
}