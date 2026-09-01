package com.semihsahinoglu.sportseus.team.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.team.dto.TeamResponse
import com.semihsahinoglu.sportseus.team.service.TeamService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/teams")
class TeamController(
    val teamService: TeamService
) {
    // Tek takım (venue gömülü)
    @GetMapping("/{externalId}")
    fun getTeam(@PathVariable externalId: Int): ResponseEntity<ApiResponse<TeamResponse>> {
        val team = teamService.getByExternalId(externalId)
        val response = ApiResponse.success(team)
        return ResponseEntity.ok(response)
    }
}