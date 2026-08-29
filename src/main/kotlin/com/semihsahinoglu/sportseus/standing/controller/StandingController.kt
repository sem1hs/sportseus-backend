package com.semihsahinoglu.sportseus.standing.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.standing.dto.StandingResponse
import com.semihsahinoglu.sportseus.standing.service.StandingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/standings")
class StandingController(
    private val standingService: StandingService
) {
    // PUBLIC: bir ligin puan tablosu (rank sırasına göre)
    @GetMapping("/leagues/{leagueExternalId}")
    fun getByLeague(
        @PathVariable leagueExternalId: Int,
        @RequestParam season: Int,
    ): ResponseEntity<ApiResponse<List<StandingResponse>>> {
        val standing = standingService.getByLeagueAndSeason(leagueExternalId, season)
        val response = ApiResponse.success(standing)
        return ResponseEntity.ok(response)
    }

    // PUBLIC: bir takımın bir ligdeki sıralaması
    @GetMapping("/teams/{teamExternalId}")
    fun getByTeam(
        @PathVariable teamExternalId: Int,
        @RequestParam leagueId: Int,
        @RequestParam season: Int,
    ): ResponseEntity<ApiResponse<StandingResponse>> {
        val standing = standingService.getByTeam(teamExternalId, leagueId, season)
        val response = ApiResponse.success(standing)
        return ResponseEntity.ok(response)
    }
}
