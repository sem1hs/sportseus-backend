package com.semihsahinoglu.sportseus.coach.controller

import com.semihsahinoglu.sportseus.coach.dto.CoachResponse
import com.semihsahinoglu.sportseus.coach.service.CoachService
import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/coaches")
class CoachController(
    private val coachService: CoachService
) {

    // PUBLIC: tek coach profili
    @GetMapping("/{id}")
    fun getCoach(@PathVariable id: UUID): ResponseEntity<ApiResponse<CoachResponse>> {
        val coach = coachService.getByExternalId(id)
        val response = ApiResponse.success(coach)
        return ResponseEntity.ok(response)
    }

    // PUBLIC: bir takımda görev yapmış coach'lar
    @GetMapping("/teams/{teamExternalId}")
    fun getByTeam(@PathVariable teamExternalId: Int): ResponseEntity<ApiResponse<List<CoachResponse>>> {
        val coach = coachService.getByTeamExternalId(teamExternalId)
        val response = ApiResponse.success(coach)
        return ResponseEntity.ok(response)
    }
}