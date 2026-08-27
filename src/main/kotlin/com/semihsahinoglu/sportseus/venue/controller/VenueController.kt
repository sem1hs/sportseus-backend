package com.semihsahinoglu.sportseus.venue.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.venue.dto.VenueResponse
import com.semihsahinoglu.sportseus.venue.service.VenueService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/venues")
class VenueController(
    private val venueService: VenueService
) {
    // PUBLIC: tek venue
    @GetMapping("/{externalId}")
    fun getByExternalId(@PathVariable externalId: Int): ResponseEntity<ApiResponse<VenueResponse>> {
        val venue = venueService.getByExternalId(externalId)
        val response = ApiResponse.success(venue)
        return ResponseEntity.ok(response)
    }
}