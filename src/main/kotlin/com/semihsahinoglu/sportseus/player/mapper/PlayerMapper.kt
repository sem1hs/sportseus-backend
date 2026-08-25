package com.semihsahinoglu.sportseus.player.mapper

import com.semihsahinoglu.sportseus.player.dto.PlayerNode
import com.semihsahinoglu.sportseus.player.dto.PlayerResponse
import com.semihsahinoglu.sportseus.player.dto.squad.SquadPlayerNode
import com.semihsahinoglu.sportseus.player.entity.Player
import org.springframework.stereotype.Component

@Component
class PlayerMapper {

    fun toEntity(node: PlayerNode): Player =
        Player(
            externalId = requireNotNull(node.id) { "Player id (external) null olamaz" },
            name = node.name ?: "",
            firstName = node.firstname,
            lastName = node.lastname,
            age = node.age,
            birthDate = node.birth?.date,
            birthPlace = node.birth?.place,
            birthCountry = node.birth?.country,
            nationality = node.nationality,
            height = node.height,
            weight = node.weight,
            photo = node.photo,
        )

    fun applyApiData(target: Player, node: PlayerNode) {
        // external_id değişmez (kimlik). name null gelirse mevcut adı koru.
        node.name?.let { target.name = it }
        target.firstName = node.firstname
        target.lastName = node.lastname
        target.age = node.age
        target.birthDate = node.birth?.date
        target.birthPlace = node.birth?.place
        target.birthCountry = node.birth?.country
        target.nationality = node.nationality
        target.height = node.height
        target.weight = node.weight
        target.photo = node.photo
    }

    // ── Minimal stub (/players/squads) ──────────────────────
    // Sadece Player yoksa çağrılır; profil sync'i sonra zenginleştirir.
    fun toStubEntity(node: SquadPlayerNode): Player =
        Player(
            externalId = requireNotNull(node.id) { "Squad player id (external) null olamaz" },
            name = node.name ?: "",
            age = node.age,
            photo = node.photo,
            // firstName/lastName/birth/nationality/height/weight → profil sync dolduracak
        )

    fun toResponse(player: Player): PlayerResponse =
        PlayerResponse(
            id = player.id!!,
            externalId = player.externalId,
            name = player.name,
            firstName = player.firstName,
            lastName = player.lastName,
            age = player.age,
            birthDate = player.birthDate,
            birthPlace = player.birthPlace,
            birthCountry = player.birthCountry,
            nationality = player.nationality,
            height = player.height,
            weight = player.weight,
            photo = player.photo,
        )
}