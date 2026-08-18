package com.semihsahinoglu.sportseus.league.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.league.dto.LeagueResponse
import com.semihsahinoglu.sportseus.league.service.LeagueService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/leagues")
class LeagueController(
    private val leagueService: LeagueService
) {

    @GetMapping("/{externalId}")
    fun getLeague(
        @PathVariable externalId: Int,
        @RequestParam(defaultValue = "2024") season: Int,
    ): ResponseEntity<ApiResponse<LeagueResponse>> {
        val league = leagueService.getByExternalIdAndSeason(externalId, season)
        val response = ApiResponse.success(league)
        return ResponseEntity.ok(response)
    }

    @GetMapping
    fun getLeagues(@RequestParam(defaultValue = "2024") season: Int): ResponseEntity<ApiResponse<List<LeagueResponse>>> {
        val leagues = leagueService.getAllBySeason(season)
        val response = ApiResponse.success(leagues)
        return ResponseEntity.ok(response)
    }

}