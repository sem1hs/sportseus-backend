package com.semihsahinoglu.sportseus.coach.service

import com.semihsahinoglu.sportseus.coach.client.CoachApiClient
import com.semihsahinoglu.sportseus.coach.dto.CoachResponse
import com.semihsahinoglu.sportseus.coach.mapper.CoachMapper
import com.semihsahinoglu.sportseus.coach.repository.CoachCareerRepository
import org.springframework.stereotype.Service

@Service
class CoachTeamSyncService(
    private val coachApiClient: CoachApiClient,
    private val coachService: CoachService,
    private val coachCareerRepository: CoachCareerRepository,
    private val coachMapper: CoachMapper,
) {
    // ADMIN: takımdaki tüm coach'ları sync (/coachs?team=) — coach-başı izolasyon
    fun syncByTeam(teamExternalId: Int): List<CoachResponse> {
        val items = coachApiClient.fetchByTeam(teamExternalId)

        return items.mapNotNull { item ->
            try {
                val coach = coachService.syncOneCoach(item)      // farklı bean → proxy → ayrı tx
                val careers = coachCareerRepository.findAllByCoachIdOrderByStartDateDesc(coach.id!!)
                coachMapper.toResponse(coach, careers)
            } catch (e: Exception) {
                null
            }
        }
    }
}