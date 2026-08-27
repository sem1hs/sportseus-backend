package com.semihsahinoglu.sportseus.fixture.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.fixture.dto.FixtureResponse
import com.semihsahinoglu.sportseus.fixture.service.FixtureService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/fixtures")
class FixtureController(
    private val fixtureService: FixtureService,
) {
    // PUBLIC: tek maç detayı
    @GetMapping("/{externalId}")
    fun getByExternalId(@PathVariable externalId: Long): ResponseEntity<ApiResponse<FixtureResponse>> {
        val fixture = fixtureService.getByExternalId(externalId)
        val response = ApiResponse.success(fixture)
        return ResponseEntity.ok(response)
    }

    // PUBLIC: bir ligin bir sezondaki maçları
    @GetMapping("/leagues/{leagueExternalId}")
    fun getByLeague(
        @PathVariable leagueExternalId: Int,
        @RequestParam season: Int,
    ): ResponseEntity<ApiResponse<List<FixtureResponse>>> {
        val fixture = fixtureService.getByLeagueAndSeason(leagueExternalId, season)
        val response = ApiResponse.success(fixture)
        return ResponseEntity.ok(response)
    }

    // PUBLIC: bir takımın bir sezondaki maçları
    @GetMapping("/teams/{teamExternalId}")
    fun getByTeam(
        @PathVariable teamExternalId: Int,
        @RequestParam season: Int,
    ): ResponseEntity<ApiResponse<List<FixtureResponse>>> {
        val fixture = fixtureService.getByTeamAndSeason(teamExternalId, season)
        val response = ApiResponse.success(fixture)
        return ResponseEntity.ok(response)
    }
}