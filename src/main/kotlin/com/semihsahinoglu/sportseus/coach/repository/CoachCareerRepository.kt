package com.semihsahinoglu.sportseus.coach.repository

import com.semihsahinoglu.sportseus.coach.entity.Coach
import com.semihsahinoglu.sportseus.coach.entity.CoachCareer
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.UUID

@Repository
interface CoachCareerRepository : JpaRepository<CoachCareer, UUID> {

    // Career upsert'ünün kalbi — (coach, team, start) ile bul
    fun findByCoachIdAndTeamExternalIdAndStartDate(
        coachId: UUID,
        teamExternalId: Int,
        startDate: LocalDate,
    ): CoachCareer?

    // READ: bir coach'un tüm kariyeri (en yeni dönem üstte)
    // coach snapshot olduğu için ilişki JOIN'i gerekmiyor (team FK yok)
    fun findAllByCoachExternalIdOrderByStartDateDesc(coachExternalId: Int): List<CoachCareer>

    // READ: bir coach'un career'ı (coach entity üzerinden)
    fun findAllByCoachIdOrderByStartDateDesc(coachId: UUID): List<CoachCareer>

    @Query("""
    SELECT c FROM CoachCareer c
    JOIN FETCH c.coach
    WHERE c.coach IN (
        SELECT DISTINCT cc.coach FROM CoachCareer cc WHERE cc.teamExternalId = :teamExternalId
    )
    ORDER BY c.startDate DESC
""")
    fun findAllCareersByTeamExternalId(@Param("teamExternalId") teamExternalId: Int): List<CoachCareer>
}