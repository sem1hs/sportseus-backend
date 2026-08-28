package com.semihsahinoglu.sportseus.coach.controller

import com.semihsahinoglu.sportseus.coach.dto.CoachResponse
import com.semihsahinoglu.sportseus.coach.dto.CoachUpdateRequest
import com.semihsahinoglu.sportseus.coach.service.CoachService
import com.semihsahinoglu.sportseus.coach.service.CoachTeamSyncService
import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/coaches")
class CoachAdminController(
    private val coachService: CoachService,
    private val coachTeamSyncService: CoachTeamSyncService
) {
    // ADMIN: tek coach sync (/coachs?id=)
    @PostMapping("/{externalId}/sync")
    fun syncById(@PathVariable externalId: Int): ResponseEntity<ApiResponse<CoachResponse>> {
        val coach = coachService.syncById(externalId)
        val response = ApiResponse.success(coach)
        return ResponseEntity.ok(response)
    }

    // ADMIN: takımdaki tüm coach'ları sync (/coachs?team=)
    @PostMapping("/sync/teams/{teamExternalId}")
    fun syncByTeam(@PathVariable teamExternalId: Int): ResponseEntity<ApiResponse<List<CoachResponse>>> {
        val coach = coachTeamSyncService.syncByTeam(teamExternalId)
        val response = ApiResponse.success(coach)
        return ResponseEntity.ok(response)
    }

    // ADMIN: elle güncelleme (partial)
    @PatchMapping("/{externalId}")
    fun update(
        @PathVariable externalId: Int,
        @RequestBody request: CoachUpdateRequest,
    ): ResponseEntity<ApiResponse<CoachResponse>> {
        val coach = coachService.update(externalId, request)
        val response = ApiResponse.success(coach)
        return ResponseEntity.ok(response)
    }

    // ADMIN: hard delete
    @DeleteMapping("/{externalId}")
    fun delete(@PathVariable externalId: Int): ResponseEntity<Void> {
        coachService.deleteByExternalId(externalId)
        return ResponseEntity.noContent().build()
    }
}