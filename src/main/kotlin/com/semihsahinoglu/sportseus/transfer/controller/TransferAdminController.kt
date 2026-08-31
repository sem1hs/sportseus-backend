package com.semihsahinoglu.sportseus.transfer.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.transfer.dto.TransferCreateRequest
import com.semihsahinoglu.sportseus.transfer.dto.TransferResponse
import com.semihsahinoglu.sportseus.transfer.dto.TransferUpdateRequest
import com.semihsahinoglu.sportseus.transfer.facade.TransferSyncFacade
import com.semihsahinoglu.sportseus.transfer.service.TransferService
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
@RequestMapping("/admin/transfers")
class TransferAdminController(
    private val transferSyncFacade: TransferSyncFacade,
    private val transferService: TransferService,
) {
    // ADMIN: oyuncuya göre sync (?player=)
    @PostMapping("/sync/players/{playerExternalId}")
    fun syncByPlayer(@PathVariable playerExternalId: Long): ResponseEntity<ApiResponse<List<TransferResponse>>> {
        val transfers = transferSyncFacade.syncByPlayer(playerExternalId)
        val response = ApiResponse.success(transfers)
        return ResponseEntity.ok(response)
    }

    // ADMIN: takıma göre sync (?team=)
    @PostMapping("/sync/teams/{teamExternalId}")
    fun syncByTeam(@PathVariable teamExternalId: Int): ResponseEntity<ApiResponse<List<TransferResponse>>> {
        val transfers = transferSyncFacade.syncByTeam(teamExternalId)
        val response = ApiResponse.success(transfers)
        return ResponseEntity.ok(response)
    }

    // ADMIN: elle transfer ekleme
    @PostMapping
    fun create(@RequestBody request: TransferCreateRequest): ResponseEntity<ApiResponse<TransferResponse>> {
        val transfers = transferService.create(request)
        val response = ApiResponse.success(transfers)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    // ADMIN: elle güncelleme (partial)
    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody request: TransferUpdateRequest,
    ): ResponseEntity<ApiResponse<TransferResponse>> {
        val updatedTransfer = transferService.update(id, request)
        val response = ApiResponse.success(updatedTransfer)
        return ResponseEntity.ok(response)
    }

    // ADMIN: tekil silme
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        transferService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}