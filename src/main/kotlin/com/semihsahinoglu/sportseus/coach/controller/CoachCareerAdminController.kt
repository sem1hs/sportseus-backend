package com.semihsahinoglu.sportseus.coach.controller

import com.semihsahinoglu.sportseus.coach.dto.CoachCareerCreateRequest
import com.semihsahinoglu.sportseus.coach.dto.CoachCareerUpdateRequest
import com.semihsahinoglu.sportseus.coach.dto.CoachResponse
import com.semihsahinoglu.sportseus.coach.service.CoachCareerService
import com.semihsahinoglu.sportseus.common.dto.ApiResponse
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
@RequestMapping("/admin/coaches/career")
class CoachCareerAdminController(
    private val coachCareerService: CoachCareerService
) {
    // ADMIN: coach'a career ekle
    @PostMapping("/{coachId}")
    fun addCareer(
        @PathVariable coachId: UUID,
        @RequestBody request: CoachCareerCreateRequest,
    ): ResponseEntity<ApiResponse<CoachResponse>> {
        val coachCareer = coachCareerService.addCareer(coachId, request)
        val response = ApiResponse.success(coachCareer)
        return ResponseEntity.ok(response)
    }

    // ADMIN: career güncelle
    @PatchMapping("/{careerId}/coach/{coachId}")
    fun updateCareer(
        @PathVariable coachId: UUID,
        @PathVariable careerId: UUID,
        @RequestBody request: CoachCareerUpdateRequest,
    ): ResponseEntity<ApiResponse<CoachResponse>> {
        val coachCareer = coachCareerService.updateCareer(coachId, careerId, request)
        val response = ApiResponse.success(coachCareer)
        return ResponseEntity.ok(response)
    }

    // ADMIN: career sil
    @DeleteMapping("/{careerId}/coach/{coachId}")
    fun deleteCareer(
        @PathVariable coachId: UUID,
        @PathVariable careerId: UUID,
    ): ResponseEntity<Void> {
        coachCareerService.deleteCareer(coachId, careerId)
        return ResponseEntity.noContent().build()
    }
}