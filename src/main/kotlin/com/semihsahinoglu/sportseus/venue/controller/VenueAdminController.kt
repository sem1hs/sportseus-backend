package com.semihsahinoglu.sportseus.venue.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.venue.dto.VenueResponse
import com.semihsahinoglu.sportseus.venue.dto.VenueUpdateRequest
import com.semihsahinoglu.sportseus.venue.service.VenueService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/venues")
class VenueAdminController(
    private val venueService: VenueService
) {
    // ADMIN: venue sync (/venues?id=)
    @PostMapping("/{externalId}/sync")
    fun sync(@PathVariable externalId: Int): ResponseEntity<ApiResponse<VenueResponse>> {
        val venue = venueService.syncById(externalId)
        val response = ApiResponse.success(venue)
        return ResponseEntity.ok(response)
    }

    // ADMIN: venue güncelle
    @PatchMapping("/{externalId}")
    fun update(
        @PathVariable externalId: Int,
        @RequestBody request: VenueUpdateRequest,
    ): ResponseEntity<ApiResponse<VenueResponse>> {
        val venue = venueService.update(externalId, request)
        val response = ApiResponse.success(venue)
        return ResponseEntity.ok(response)
    }
}