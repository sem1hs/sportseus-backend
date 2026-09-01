package com.semihsahinoglu.sportseus.team.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.team.dto.statistics.TeamStatisticsNode
import com.semihsahinoglu.sportseus.team.dto.statistics.TeamStatisticsResponse
import com.semihsahinoglu.sportseus.team.service.TeamStatisticsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/teams/statistics")
class TeamStatisticsController(
    private val teamStatisticsService: TeamStatisticsService
) {
    // GET /teams/statistics/{teamId}?league={leagueId}&season=2024
    @GetMapping("/{teamId}/league/{leagueId}")
    fun getStatisticsByLeagueId(
        @PathVariable teamId: UUID,
        @PathVariable leagueId: UUID,
        @RequestParam season: Int,
    ): ResponseEntity<ApiResponse<TeamStatisticsResponse>> {
        val teamStatistic = teamStatisticsService.getStatistics(teamId, leagueId, season)
        val response = ApiResponse.success(teamStatistic)
        return ResponseEntity.ok(response)
    }

    // GET /teams/statistics/{teamId}?league={leagueId}&season=2024
    @GetMapping("/{teamId}")
    fun getStatistics(
        @PathVariable teamId: UUID,
        @RequestParam season: Int,
    ): ResponseEntity<ApiResponse<TeamStatisticsResponse>> {
        val teamStatistic = teamStatisticsService.getStatistics(teamId, season)
        val response = ApiResponse.success(teamStatistic)
        return ResponseEntity.ok(response)
    }
}