package com.semihsahinoglu.sportseus.lineup.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.lineup.dto.LineupResponse
import com.semihsahinoglu.sportseus.lineup.service.LineupService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/lineups")
class LineupController(
    private val lineupService: LineupService
) {
    // PUBLIC: bir maçın dizilişleri (home + away)
    @GetMapping("/fixtures/{fixtureId}")
    fun getByFixture(@PathVariable fixtureId: UUID): ResponseEntity<ApiResponse<List<LineupResponse>>> {
        val lineup = lineupService.getByFixture(fixtureId)
        val response = ApiResponse.success(lineup)
        return ResponseEntity.ok(response)
    }
}