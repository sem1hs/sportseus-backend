package com.semihsahinoglu.sportseus.coach.repository

import com.semihsahinoglu.sportseus.coach.entity.Coach
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CoachRepository : JpaRepository<Coach, UUID> {

    // Upsert'ün kalbi + lookup
    fun findByExternalId(externalId: Int): Coach?
}