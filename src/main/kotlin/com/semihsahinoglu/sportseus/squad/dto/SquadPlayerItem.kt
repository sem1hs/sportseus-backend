package com.semihsahinoglu.sportseus.squad.dto

import java.time.LocalDate
import java.util.UUID

data class SquadPlayerItem(
    val playerTeamId: UUID,     // üyelik kaydının id'si (gerekirse silme/detay için)
    val id: UUID,               // Player'ın bizdeki id'si
    val externalId: Long?,       // Player'ın API id'si
    val name: String,
    val birthDate: LocalDate?,
    val photo: String?,
    val number: Int?,           // PlayerTeam'den (squad'ın doldurduğu)
    val position: String?,      // PlayerTeam'den
    val nationality:String?
)