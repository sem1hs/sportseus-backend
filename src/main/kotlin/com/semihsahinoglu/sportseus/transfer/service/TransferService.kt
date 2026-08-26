package com.semihsahinoglu.sportseus.transfer.service

import com.semihsahinoglu.sportseus.player.entity.Player
import com.semihsahinoglu.sportseus.team.entity.Team
import com.semihsahinoglu.sportseus.team.service.TeamService
import com.semihsahinoglu.sportseus.transfer.dto.TransferResponse
import com.semihsahinoglu.sportseus.transfer.dto.TransferUpdateRequest
import com.semihsahinoglu.sportseus.transfer.exception.TransferConflictException
import com.semihsahinoglu.sportseus.transfer.exception.TransferNotFoundException
import com.semihsahinoglu.sportseus.transfer.mapper.TransferMapper
import com.semihsahinoglu.sportseus.transfer.repository.TransferRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.UUID

@Service
class TransferService(
    private val transferRepository: TransferRepository,
    private val transferMapper: TransferMapper,
    private val teamService: TeamService,
) {

    // SYNC: tek transfer upsert (facade döngüden çağırır, per-transfer tx)
    @Transactional
    fun upsertOne(
        player: Player,
        teamIn: Team,
        teamOut: Team,
        date: LocalDate,
        rawType: String,
    ): TransferResponse {
        val existing = transferRepository.findByPlayerIdAndDateAndTeamInIdAndTeamOutId(
            player.id!!,
            date,
            teamIn.id!!,
            teamOut.id!!
        )

        val saved = when {
            existing == null -> transferRepository.save(transferMapper.toEntity(player, teamIn, teamOut, date, rawType))
            existing.manuallyEdited -> existing
            else -> {
                transferMapper.applyApiData(existing, teamIn, teamOut, rawType)
                transferRepository.save(existing)
            }
        }
        return transferMapper.toResponse(saved)
    }

    // ADMIN: elle güncelleme (partial, manuallyEdited=true)
    @Transactional
    fun update(id: UUID, request: TransferUpdateRequest): TransferResponse {
        val transfer =
            transferRepository.findById(id).orElseThrow { TransferNotFoundException("Transfer bulunamadı: id=$id") }

        // team değişimi → KATI FK (olmayan team → hata, sync'teki gibi atlama YOK)
        val teamIn = request.teamInExternalId?.let { extId ->
            teamService.findByExternalIdOptional(extId)
                ?: throw TransferNotFoundException("Takım bulunamadı (in): team=$extId")
        }

        val teamOut = request.teamOutExternalId?.let { extId ->
            teamService.findByExternalIdOptional(extId)
                ?: throw TransferNotFoundException("Takım bulunamadı (out): team=$extId")
        }

        transfer.applyManualUpdate(
            date = request.date,
            rawType = request.rawType,
            transferType = request.transferType,
            fee = request.fee,
            teamIn = teamIn,
            teamOut = teamOut,
        )

        val updated = try {
            transferRepository.saveAndFlush(transfer)
        } catch (e: DataIntegrityViolationException) {
            throw TransferConflictException(
                "Bu güncelleme mevcut bir transferle çakışıyor (aynı oyuncu, tarih, in/out): id=$id"
            )
        }

        return transferMapper.toResponse(updated)
    }

    // ADMIN: hard delete (mükerrer/yanlış kaydı elle temizle)
    @Transactional
    fun deleteById(id: UUID) {
        if (!transferRepository.existsById(id)) throw TransferNotFoundException("Transfer bulunamadı: id=$id")
        transferRepository.deleteById(id)
    }

    // PUBLIC: oyuncunun transfer geçmişi
    @Transactional(readOnly = true)
    fun getByPlayerExternalId(playerExternalId: Long): List<TransferResponse> =
        transferRepository.findAllByPlayerExternalIdOrderByDateDesc(playerExternalId)
            .map(transferMapper::toResponse)

    // PUBLIC: takımın hareketleri (gelen + giden)
    @Transactional(readOnly = true)
    fun getByTeamExternalId(teamExternalId: Int): List<TransferResponse> =
        transferRepository.findAllByTeamExternalId(teamExternalId)
            .map(transferMapper::toResponse)
}