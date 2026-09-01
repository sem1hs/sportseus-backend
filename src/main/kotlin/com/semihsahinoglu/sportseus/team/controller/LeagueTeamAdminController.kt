package com.semihsahinoglu.sportseus.team.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.team.dto.LeagueTeamCreateRequest
import com.semihsahinoglu.sportseus.team.dto.LeagueTeamResponse
import com.semihsahinoglu.sportseus.team.dto.LeagueTeamUpdateRequest
import com.semihsahinoglu.sportseus.team.service.LeagueTeamService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/admin/teams/leagues")
class LeagueTeamAdminController(
    private val leagueTeamService: LeagueTeamService
) {

    // ADMIN: elle league teams ekleme
    @PostMapping
    fun createLeagueTeam(@RequestBody request: LeagueTeamCreateRequest): ResponseEntity<ApiResponse<LeagueTeamResponse>> {
        val leagueTeam = leagueTeamService.createLeagueTeam(request)
        val response = ApiResponse.success(leagueTeam)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    // ADMIN: elle partial update
    @PatchMapping("/{id}")
    fun updateLeagueTeam(
        @PathVariable id: UUID,
        @RequestBody request: LeagueTeamUpdateRequest
    ): ResponseEntity<ApiResponse<LeagueTeamResponse>> {
        val leagueTeam = leagueTeamService.update(id, request)
        val response = ApiResponse.success(leagueTeam)
        return ResponseEntity.status(HttpStatus.OK).body(response)
    }

    // ADMIN: hard delete
    @DeleteMapping("/{id}")
    fun deleteLeagueTeam(@PathVariable id: UUID): ResponseEntity<Void> {
        leagueTeamService.deleteLeagueTeam(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}