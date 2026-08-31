package com.semihsahinoglu.sportseus.lineup.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.lineup.dto.LineupCreateRequest
import com.semihsahinoglu.sportseus.lineup.dto.LineupPlayerAddRequest
import com.semihsahinoglu.sportseus.lineup.dto.LineupPlayerUpdateRequest
import com.semihsahinoglu.sportseus.lineup.dto.LineupResponse
import com.semihsahinoglu.sportseus.lineup.dto.LineupUpdateRequest
import com.semihsahinoglu.sportseus.lineup.service.LineupFixtureSyncService
import com.semihsahinoglu.sportseus.lineup.service.LineupPlayerService
import com.semihsahinoglu.sportseus.lineup.service.LineupService
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
@RequestMapping("/admin/lineups")
class LineupAdminController(
    private val lineupFixtureSyncService: LineupFixtureSyncService,
    private val lineupService: LineupService,
    private val lineupPlayerService: LineupPlayerService,
) {
    // ADMIN: maçın dizilişlerini sync (/fixtures/lineups?fixture=)
    @PostMapping("/sync/fixtures/{fixtureExternalId}")
    fun syncByFixture(@PathVariable fixtureExternalId: Long): ResponseEntity<ApiResponse<List<LineupResponse>>> {
        val lineup = lineupFixtureSyncService.syncByFixture(fixtureExternalId)
        val response = ApiResponse.success(lineup)
        return ResponseEntity.ok(response)
    }

    // ADMIN: elle güncelleme (formation/coach + oyuncular replace)
    @PatchMapping("/fixtures/{fixtureId}/teams/{teamExternalId}")
    fun update(
        @PathVariable fixtureId: UUID,
        @PathVariable teamExternalId: Int,
        @RequestBody request: LineupUpdateRequest,
    ): ResponseEntity<ApiResponse<LineupResponse>> {
        val lineup = lineupService.update(fixtureId, teamExternalId, request)
        val response = ApiResponse.success(lineup)
        return ResponseEntity.ok(response)
    }

    // ADMIN: oyuncuyu lineup'tan sil
    @DeleteMapping("/fixtures/{fixtureId}/teams/{teamExternalId}/players/{lineupPlayerId}")
    fun deletePlayer(
        @PathVariable fixtureId: UUID,
        @PathVariable teamExternalId: Int,
        @PathVariable lineupPlayerId: UUID,
    ): ResponseEntity<Void> {
        lineupPlayerService.deletePlayer(fixtureId, teamExternalId, lineupPlayerId)
        return ResponseEntity.noContent().build()
    }

    // ADMIN: oyuncu güncelle (number/position/isStarter — yedeğe al dahil)
    @PatchMapping("/fixtures/{fixtureId}/teams/{teamExternalId}/players/{lineupPlayerId}")
    fun updatePlayer(
        @PathVariable fixtureId: UUID,
        @PathVariable teamExternalId: Int,
        @PathVariable lineupPlayerId: UUID,
        @RequestBody request: LineupPlayerUpdateRequest,
    ): ResponseEntity<ApiResponse<LineupResponse>> {
        val player = lineupPlayerService.updatePlayer(
            fixtureId,
            teamExternalId,
            lineupPlayerId,
            request
        )
        val response = ApiResponse.success(player)
        return ResponseEntity.ok(response)
    }

    // ADMIN: lineup'a oyuncu ekle
    @PostMapping("/fixtures/{fixtureId}/teams/{teamExternalId}/players")
    fun addPlayer(
        @PathVariable fixtureId: UUID,
        @PathVariable teamExternalId: Int,
        @RequestBody request: LineupPlayerAddRequest,
    ): ResponseEntity<ApiResponse<LineupResponse>> {
        val player = lineupPlayerService.addPlayer(fixtureId, teamExternalId, request)
        val response = ApiResponse.success(player)
        return ResponseEntity.ok(response)
    }

    // AdminLineupController'a ekle
    @PostMapping
    fun create(@RequestBody request: LineupCreateRequest): ResponseEntity<ApiResponse<LineupResponse>> {
        val lineup = lineupService.create(request)
        val response = ApiResponse.success(lineup)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    // ADMIN: tekil silme
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        lineupService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}