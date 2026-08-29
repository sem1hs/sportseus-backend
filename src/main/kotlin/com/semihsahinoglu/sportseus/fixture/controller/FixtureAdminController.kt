package com.semihsahinoglu.sportseus.fixture.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.fixture.dto.FixtureCreateRequest
import com.semihsahinoglu.sportseus.fixture.dto.FixtureResponse
import com.semihsahinoglu.sportseus.fixture.dto.FixtureUpdateRequest
import com.semihsahinoglu.sportseus.fixture.service.FixtureService
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
@RequestMapping("/admin/fixtures")
class FixtureAdminController(
    private val fixtureService: FixtureService,
) {
    // ADMIN: takımın ligdeki sezon maçlarını sync et
    @PostMapping("/sync")
    fun sync(
        @RequestParam leagueId: Int,
        @RequestParam season: Int,
        @RequestParam teamId: Int,
    ): ResponseEntity<ApiResponse<List<FixtureResponse>>> {
        val fixtures = fixtureService.sync(leagueId, season, teamId)
        val response = ApiResponse.success(fixtures)
        return ResponseEntity.ok(response)
    }

    // ADMIN: elle fixture ekle (katı FK)
    @PostMapping
    fun create(@RequestBody request: FixtureCreateRequest): ResponseEntity<ApiResponse<FixtureResponse>> {
        val fixture = fixtureService.create(request)
        val response = ApiResponse.success(fixture)
        return ResponseEntity.ok(response)
    }

    // ADMIN: elle güncelleme (UUID, partial + FK değişimi)
    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody request: FixtureUpdateRequest,
    ): ResponseEntity<ApiResponse<FixtureResponse>> {
        val fixture = fixtureService.update(id, request)
        val response = ApiResponse.success(fixture)
        return ResponseEntity.ok(response)
    }

    // ADMIN: tekil silme
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        fixtureService.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    // ADMIN: fixture'a venue bağla
    @PutMapping("/{id}/venue/{venueExternalId}")
    fun updateVenue(
        @PathVariable id: UUID,
        @PathVariable venueExternalId: Int,
    ): ResponseEntity<ApiResponse<FixtureResponse>> {
        val updatedVenue = fixtureService.updateVenue(id, venueExternalId)
        val response = ApiResponse.success(updatedVenue)
        return ResponseEntity.ok(response)
    }
}