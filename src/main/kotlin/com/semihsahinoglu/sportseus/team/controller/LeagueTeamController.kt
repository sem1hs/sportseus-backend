package com.semihsahinoglu.sportseus.team.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.team.dto.LeagueTeamResponse
import com.semihsahinoglu.sportseus.team.service.LeagueTeamService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/teams/leagues")
class LeagueTeamController(
    private val leagueTeamService: LeagueTeamService
) {

    // PUBLIC: season ve leagueye göre team görüntüleme
    @GetMapping("{id}")
    fun getLeagueTeams(
        @PathVariable id: UUID,
        @RequestParam season: Int
    ): ResponseEntity<ApiResponse<List<LeagueTeamResponse>>> {
        val leagueTeams = leagueTeamService.getLeagueTeamByLeagueIdAndSeason(id, season)
        val response = ApiResponse.success(leagueTeams)
        return ResponseEntity.ok(response)
    }
}