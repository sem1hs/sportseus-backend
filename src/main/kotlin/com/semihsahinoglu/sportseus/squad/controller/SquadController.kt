package com.semihsahinoglu.sportseus.squad.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.squad.dto.SquadResponse
import com.semihsahinoglu.sportseus.squad.service.SquadService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/squads")
class SquadController(
    private val squadService: SquadService
) {
    // PUBLIC: seasona göre kadro döner
    @GetMapping("/{teamExternalId}")
    fun getSquad(
        @PathVariable teamExternalId: Int,
        @RequestParam season: Int
    ): ResponseEntity<ApiResponse<SquadResponse>> {
        val squad = squadService.getSquadByTeamExternalId(teamExternalId, season)
        val response = ApiResponse.success(squad)
        return ResponseEntity.ok(response)
    }
}