package com.semihsahinoglu.sportseus.lineup.repository

import com.semihsahinoglu.sportseus.lineup.entity.LineupPlayer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface LineupPlayerRepository : JpaRepository<LineupPlayer, UUID> {
    fun findByIdAndLineupId(id: UUID, lineupId: UUID): LineupPlayer?
}