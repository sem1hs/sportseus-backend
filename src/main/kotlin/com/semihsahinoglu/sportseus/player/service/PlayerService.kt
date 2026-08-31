package com.semihsahinoglu.sportseus.player.service

import com.semihsahinoglu.sportseus.player.client.PlayerApiClient
import com.semihsahinoglu.sportseus.player.dto.PlayerCreateRequest
import com.semihsahinoglu.sportseus.player.dto.PlayerNode
import com.semihsahinoglu.sportseus.player.dto.PlayerResponse
import com.semihsahinoglu.sportseus.player.dto.PlayerUpdateRequest
import com.semihsahinoglu.sportseus.player.dto.squad.SquadPlayerNode
import com.semihsahinoglu.sportseus.player.entity.Player
import com.semihsahinoglu.sportseus.player.exception.PlayerNotFoundException
import com.semihsahinoglu.sportseus.player.mapper.PlayerMapper
import com.semihsahinoglu.sportseus.player.repository.PlayerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class PlayerService(
    private val playerRepository: PlayerRepository,
    private val playerApiClient: PlayerApiClient,
    private val playerMapper: PlayerMapper
) {
    // ADMIN:  sync, tam profil
    @Transactional
    fun syncProfile(playerExternalId: Long): PlayerResponse {
        val node = playerApiClient.fetchProfile(playerExternalId)
            ?: throw PlayerNotFoundException("API-Football'da oyuncu profili bulunamadı: player=$playerExternalId")
        return playerMapper.toResponse(upsertFromNode(node))
    }

    // METHOD: upsert çekirdeği, profil VE stats akışı paylaşır.
    @Transactional
    fun upsertFromNode(node: PlayerNode): Player {
        val externalId = requireNotNull(node.id) { "Player id (external) null olamaz" }
        val existing = playerRepository.findByExternalId(externalId)
        return when {
            existing == null -> playerRepository.save(playerMapper.toEntity(node))
            existing.manualAdded || existing.manuallyEdited -> existing   // elle → dokunma
            else -> {
                playerMapper.applyApiData(existing, node)
                playerRepository.save(existing)
            }
        }
    }

    // ADMIN: elle player ekle (externalId null, manualAdded true)
    @Transactional
    fun create(request: PlayerCreateRequest): PlayerResponse {
        val player = playerMapper.toEntity(request)
        val saved = playerRepository.save(player)
        return playerMapper.toResponse(saved)
    }

    // ADMIN: elle güncelleme (UUID)
    @Transactional
    fun update(id: UUID, request: PlayerUpdateRequest): PlayerResponse {
        val player = playerRepository.findById(id).orElseThrow { PlayerNotFoundException("Oyuncu bulunamadı: id=$id") }

        player.applyManualUpdate(
            name = request.name, firstName = request.firstName, lastName = request.lastName,
            age = request.age, birthDate = request.birthDate, birthPlace = request.birthPlace,
            birthCountry = request.birthCountry, nationality = request.nationality,
            height = request.height, weight = request.weight, photo = request.photo,
        )
        return playerMapper.toResponse(player)
    }

    // ADMIN: stub, squad'dan oyuncu tanıt (yoksa oluştur, VARSA DOKUNMA)
    @Transactional
    fun ensureStub(node: SquadPlayerNode): Player {
        val externalId = requireNotNull(node.id) { "Squad player id null olamaz" }
        return playerRepository.findByExternalId(externalId) ?: playerRepository.save(playerMapper.toStubEntity(node))
    }

    // ADMIN: lookup, external id'den entity
    @Transactional(readOnly = true)
    fun getByExternalIdOrThrow(playerExternalId: Long): Player = playerRepository.findByExternalId(playerExternalId)
        ?: throw PlayerNotFoundException("Oyuncu DB'de yok, önce profil sync çalıştırın: player=$playerExternalId")

    // ADMIN: player silme
    @Transactional
    fun deleteById(playerId: UUID) {
        val player = playerRepository.findById(playerId)
            .orElseThrow { throw PlayerNotFoundException("Oyuncu bulunamadı: player=$playerId") }
        playerRepository.delete(player)      // DB cascade → PlayerTeam + PlayerStatistics otomatik silinir
    }

    // METHOD: playeri id ile bulma
    @Transactional(readOnly = true)
    fun findById(playerId: UUID): Player = playerRepository.findById(playerId)
        .orElseThrow { throw PlayerNotFoundException("Oyuncu bulunamadı: player=$playerId") }

    // PUBLIC: public okuma
    @Transactional(readOnly = true)
    fun getById(playerId: UUID): PlayerResponse {
        val player = playerRepository.findById(playerId)
            .orElseThrow { throw PlayerNotFoundException("Oyuncu bulunamadı: player=$playerId") }
        return playerMapper.toResponse(player)
    }
}