package com.semihsahinoglu.sportseus.player.service

import com.semihsahinoglu.sportseus.player.client.PlayerApiClient
import com.semihsahinoglu.sportseus.player.dto.PlayerNode
import com.semihsahinoglu.sportseus.player.dto.PlayerResponse
import com.semihsahinoglu.sportseus.player.dto.squad.SquadPlayerNode
import com.semihsahinoglu.sportseus.player.entity.Player
import com.semihsahinoglu.sportseus.player.exception.PlayerNotFoundException
import com.semihsahinoglu.sportseus.player.mapper.PlayerMapper
import com.semihsahinoglu.sportseus.player.repository.PlayerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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
        return if (existing != null) {
            playerMapper.applyApiData(existing, node)
            playerRepository.save(existing)
        } else {
            playerRepository.save(playerMapper.toEntity(node))
        }
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

    // PUBLIC: public okuma
    @Transactional(readOnly = true)
    fun getByExternalId(playerExternalId: Long): PlayerResponse =
        playerMapper.toResponse(getByExternalIdOrThrow(playerExternalId))
}