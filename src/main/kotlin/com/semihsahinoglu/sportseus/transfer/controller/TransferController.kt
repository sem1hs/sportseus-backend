package com.semihsahinoglu.sportseus.transfer.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.transfer.dto.TransferResponse
import com.semihsahinoglu.sportseus.transfer.service.TransferService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/transfers")
class TransferController(
    private val transferService: TransferService,
) {
    // PUBLIC: oyuncunun transfer geçmişi
    @GetMapping("/players/{playerExternalId}")
    fun getByPlayer(@PathVariable playerExternalId: Long): ResponseEntity<ApiResponse<List<TransferResponse>>> {
        val transfers = transferService.getByPlayerExternalId(playerExternalId)
        val response = ApiResponse.success(transfers)
        return ResponseEntity.ok(response)
    }

    // PUBLIC: takımın hareketleri (gelen + giden)
    @GetMapping("/teams/{teamExternalId}")
    fun getByTeam(@PathVariable teamExternalId: Int): ResponseEntity<ApiResponse<List<TransferResponse>>> {
        val transfers = transferService.getByTeamExternalId(teamExternalId)
        val response = ApiResponse.success(transfers)
        return ResponseEntity.ok(response)
    }

    // PUBLIC: oyuncunun transferleri, season
    @GetMapping("/players/{playerExternalId}/season/{season}")
    fun getByPlayerAndSeason(
        @PathVariable playerExternalId: Long,
        @PathVariable season: Int,
    ): ResponseEntity<ApiResponse<List<TransferResponse>>> {
        val transfers = transferService.getByPlayerAndSeason(playerExternalId, season)
        val response = ApiResponse.success(transfers)
        return ResponseEntity.ok(response)
    }

    // PUBLIC: takımın hareketleri, season
    @GetMapping("/teams/{teamExternalId}/season/{season}")
    fun getByTeamAndSeason(
        @PathVariable teamExternalId: Int,
        @PathVariable season: Int,
    ): ResponseEntity<ApiResponse<List<TransferResponse>>> {
        val transfers = transferService.getByTeamAndSeason(teamExternalId, season)
        val response = ApiResponse.success(transfers)
        return ResponseEntity.ok(response)
    }

    // PUBLIC: bir sezonun tüm transferleri
    @GetMapping("/seasons/{season}")
    fun getBySeason(@PathVariable season: Int): ResponseEntity<ApiResponse<List<TransferResponse>>> =
        ResponseEntity.ok(ApiResponse.success(transferService.getBySeason(season)))
}