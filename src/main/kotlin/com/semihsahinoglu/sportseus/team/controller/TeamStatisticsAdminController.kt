package com.semihsahinoglu.sportseus.team.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.team.dto.statistics.TeamStatisticsCreateRequest
import com.semihsahinoglu.sportseus.team.dto.statistics.TeamStatisticsNode
import com.semihsahinoglu.sportseus.team.dto.statistics.TeamStatisticsResponse
import com.semihsahinoglu.sportseus.team.dto.statistics.TeamStatisticsUpdateRequest
import com.semihsahinoglu.sportseus.team.service.TeamStatisticsService
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
@RequestMapping("/admin/teams/statistics")
class TeamStatisticsAdminController(
    private val teamStatisticsService: TeamStatisticsService
) {

    // POST /admin/teams/statistics/sync?team=998&league=203&season=2024
    @PostMapping("/sync")
    fun syncStatistics(
        @RequestParam teamId: Int,
        @RequestParam leagueId: Int,
        @RequestParam season: Int,
    ): ResponseEntity<ApiResponse<TeamStatisticsResponse>> {
        val teamStatistic = teamStatisticsService.syncStatistics(teamId, leagueId, season)
        val response = ApiResponse.success(teamStatistic)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    // ADMIN: elle create
    @PostMapping
    fun createStatistics(@RequestBody request: TeamStatisticsCreateRequest): ResponseEntity<ApiResponse<TeamStatisticsResponse>> {
        val teamStatistics = teamStatisticsService.create(request)
        val response = ApiResponse.success(teamStatistics)
        return ResponseEntity.ok(response)
    }

    // ADMIN: elle güncelleme
    @PatchMapping("/{id}")
    fun updateStatistics(
        @PathVariable id: UUID,
        @RequestBody request: TeamStatisticsUpdateRequest,
    ): ResponseEntity<ApiResponse<TeamStatisticsResponse>> {
        val teamStatistics = teamStatisticsService.update(id, request)
        val response = ApiResponse.success(teamStatistics)
        return ResponseEntity.ok(response)
    }

    // Hard delete
    @DeleteMapping("/{id}")
    fun deleteTeamStatistics(@PathVariable id: UUID): ResponseEntity<Void> {
        teamStatisticsService.deleteTeamStatistics(id)
        return ResponseEntity.noContent().build()
    }
}