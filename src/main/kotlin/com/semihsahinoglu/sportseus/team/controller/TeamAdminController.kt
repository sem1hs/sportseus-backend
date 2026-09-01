package com.semihsahinoglu.sportseus.team.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.team.dto.TeamResponse
import com.semihsahinoglu.sportseus.team.dto.TeamUpdateRequest
import com.semihsahinoglu.sportseus.team.service.TeamService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/admin/teams")
class TeamAdminController(
    private val teamService: TeamService
) {
    // Ligin tüm takımlarını senkronla: POST /admin/teams/sync?league=203&season=2024
    @PostMapping("/sync")
    fun syncByLeague(
        @RequestParam league: Int,
        @RequestParam season: Int,
    ): ResponseEntity<ApiResponse<List<TeamResponse>>> {
        val teamsByLeague = teamService.syncTeamsByLeague(league, season)
        val response = ApiResponse.success(teamsByLeague)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    // Tek takım senkronla: POST /admin/teams/{externalId}/sync?league=203&season=2024
    @PostMapping("/{externalId}/sync")
    fun syncSingle(
        @PathVariable externalId: Int,
        @RequestParam league: Int,
        @RequestParam season: Int,
    ): ResponseEntity<ApiResponse<TeamResponse>> {
        val singleTeam = teamService.syncSingleTeam(externalId, league, season)
        val response = ApiResponse.success(singleTeam)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    // ADMIN: takıma venue bağla — PUT /api/admin/teams/{teamExternalId}/venue/{venueExternalId}
    @PutMapping("/{teamExternalId}/venue/{venueExternalId}")
    fun updateVenue(
        @PathVariable teamExternalId: Int,
        @PathVariable venueExternalId: Int,
    ): ResponseEntity<ApiResponse<TeamResponse>> {
        val team = teamService.updateTeamVenue(teamExternalId, venueExternalId)
        val response = ApiResponse.success(team)
        return ResponseEntity.ok(response)
    }

    @PatchMapping("/{externalId}")
    fun update(
        @PathVariable externalId: Int,
        @RequestBody request: TeamUpdateRequest
    ): ResponseEntity<ApiResponse<TeamResponse>> {
        val team = teamService.update(externalId, request)
        val response = ApiResponse.success(team)
        return ResponseEntity.ok(response)
    }

    // Hard delete
    @DeleteMapping("/{id}")
    fun deleteTeam(@PathVariable id: UUID): ResponseEntity<Void> {
        teamService.deleteTeam(id)
        return ResponseEntity.noContent().build()
    }
}