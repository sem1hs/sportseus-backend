package com.semihsahinoglu.sportseus.player.repository

import com.semihsahinoglu.sportseus.player.entity.Player
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PlayerRepository : JpaRepository<Player, UUID> {

    fun findByExternalId(externalId: Long): Player?

    fun existsByExternalId(externalId: Long): Boolean
}