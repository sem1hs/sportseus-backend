package com.semihsahinoglu.sportseus.coach.service

import com.semihsahinoglu.sportseus.coach.dto.CoachCareerCreateRequest
import com.semihsahinoglu.sportseus.coach.dto.CoachCareerInput
import com.semihsahinoglu.sportseus.coach.dto.CoachCareerUpdateRequest
import com.semihsahinoglu.sportseus.coach.dto.CoachResponse
import com.semihsahinoglu.sportseus.coach.exception.CoachCareerConflictException
import com.semihsahinoglu.sportseus.coach.exception.CoachNotFoundException
import com.semihsahinoglu.sportseus.coach.mapper.CoachMapper
import com.semihsahinoglu.sportseus.coach.repository.CoachCareerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class CoachCareerService(
    private val coachCareerRepository: CoachCareerRepository,
    private val coachService: CoachService,
    private val coachMapper: CoachMapper,
) {

    // ADMIN: coach'a career ekle
    @Transactional
    fun addCareer(coachId: UUID, request: CoachCareerCreateRequest): CoachResponse {
        val coach = coachService.findCoachById(coachId)

        // aynı (team, start) zaten var mı? (unique key koruması)
        val exists = coachCareerRepository
            .findByCoachIdAndTeamExternalIdAndStartDate(coachId, request.teamExternalId, request.startDate) != null
        if (exists) throw CoachCareerConflictException(
            "Bu career zaten var: team=${request.teamExternalId} start=${request.startDate}"
        )

        coachCareerRepository.save(
            coachMapper.toManualCareerEntity(
                coach, CoachCareerInput(
                    teamExternalId = request.teamExternalId,
                    teamName = request.teamName,
                    teamLogo = request.teamLogo,
                    startDate = request.startDate,
                    endDate = request.endDate,
                )
            )
        )
        val careers = coachCareerRepository.findAllByCoachIdOrderByStartDateDesc(coachId)
        return coachMapper.toResponse(coach, careers)
    }

    // ADMIN: career güncelle
    @Transactional
    fun updateCareer(coachId: UUID, careerId: UUID, request: CoachCareerUpdateRequest): CoachResponse {
        val coach = coachService.findCoachById(coachId)
        val career = coachCareerRepository.findByIdAndCoachId(careerId, coachId)
            ?: throw CoachNotFoundException("Career bu coach'ta yok: career=$careerId")

        career.applyUpdate(
            teamExternalId = request.teamExternalId,
            teamName = request.teamName,
            teamLogo = request.teamLogo,
            startDate = request.startDate,
            endDate = request.endDate,
        )
        coachCareerRepository.save(career)
        val careers = coachCareerRepository.findAllByCoachIdOrderByStartDateDesc(coachId)
        return coachMapper.toResponse(coach, careers)
    }

    // ADMIN: career sil
    @Transactional
    fun deleteCareer(coachId: UUID, careerId: UUID) {
        val career = coachCareerRepository.findByIdAndCoachId(careerId, coachId)
            ?: throw CoachNotFoundException("Career bu coach'ta yok: career=$careerId")
        coachCareerRepository.delete(career)
    }
}