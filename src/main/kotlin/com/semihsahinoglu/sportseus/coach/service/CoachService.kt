package com.semihsahinoglu.sportseus.coach.service

import com.semihsahinoglu.sportseus.coach.client.CoachApiClient
import com.semihsahinoglu.sportseus.coach.dto.CoachApiItem
import com.semihsahinoglu.sportseus.coach.dto.CoachResponse
import com.semihsahinoglu.sportseus.coach.dto.CoachUpdateRequest
import com.semihsahinoglu.sportseus.coach.entity.Coach
import com.semihsahinoglu.sportseus.coach.exception.CoachNotFoundException
import com.semihsahinoglu.sportseus.coach.mapper.CoachMapper
import com.semihsahinoglu.sportseus.coach.repository.CoachCareerRepository
import com.semihsahinoglu.sportseus.coach.repository.CoachRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CoachService(
    private val coachRepository: CoachRepository,
    private val coachCareerRepository: CoachCareerRepository,
    private val coachApiClient: CoachApiClient,
    private val coachMapper: CoachMapper
) {

    // ADMIN: tek coach sync (/coachs?id=) — profil + career
    @Transactional
    fun syncById(coachExternalId: Int): CoachResponse {
        val item = coachApiClient.fetchById(coachExternalId)
            ?: throw CoachNotFoundException("API-Football'da coach bulunamadı: id=$coachExternalId")
        val coach = syncOneCoach(item)
        val careers = coachCareerRepository.findAllByCoachIdOrderByStartDateDesc(coach.id!!)
        return coachMapper.toResponse(coach, careers)
    }

    // METHOD: tek coach'un profil + career upsert'ü (CoachTeamSyncService de çağırır)
    @Transactional
    fun syncOneCoach(item: CoachApiItem): Coach {
        val externalId = requireNotNull(item.id) { "Coach id null olamaz" }

        // 1) profil upsert — manuallyEdited korumasıyla
        val existing = coachRepository.findByExternalId(externalId)
        val coach = when {
            existing == null -> coachRepository.save(coachMapper.toEntity(item))
            existing.manuallyEdited -> existing            // profil elle düzenlenmiş → ezme
            else -> {
                coachMapper.applyApiData(existing, item)
                coachRepository.save(existing)
            }
        }

        // 2) career upsert — manuallyEdited'ten BAĞIMSIZ (career her zaman tazelenir)
        upsertCareers(coach, item)

        return coach
    }

    // METHOD: career upsert — bozuk node'u atla (team/start null), gerisini yaz
    private fun upsertCareers(coach: Coach, item: CoachApiItem) {
        item.career.forEach { node ->
            if (node.team?.id == null || node.start == null) return@forEach

            val existing = coachCareerRepository.findByCoachIdAndTeamExternalIdAndStartDate(
                coach.id!!, node.team!!.id!!, node.start!!
            )
            if (existing != null) {
                coachMapper.applyCareerData(existing, node)
                coachCareerRepository.save(existing)
            } else {
                coachCareerRepository.save(coachMapper.toCareerEntity(coach, node))
            }
        }
    }

    // ADMIN: elle güncelleme (partial, manuallyEdited=true)
    @Transactional
    fun update(coachExternalId: Int, request: CoachUpdateRequest): CoachResponse {
        val coach = coachRepository.findByExternalId(coachExternalId)
            ?: throw CoachNotFoundException("Coach bulunamadı: id=$coachExternalId")

        coach.applyManualUpdate(
            name = request.name,
            firstName = request.firstName,
            lastName = request.lastName,
            age = request.age,
            birthDate = request.birthDate,
            birthPlace = request.birthPlace,
            birthCountry = request.birthCountry,
            nationality = request.nationality,
            height = request.height,
            weight = request.weight,
            photo = request.photo,
        )

        val saved = coachRepository.save(coach)
        val careers = coachCareerRepository.findAllByCoachIdOrderByStartDateDesc(saved.id!!)
        return coachMapper.toResponse(saved, careers)
    }

    // PUBLIC: tek coach (profil + career)
    @Transactional(readOnly = true)
    fun getByExternalId(coachExternalId: Int): CoachResponse {
        val coach = coachRepository.findByExternalId(coachExternalId)
            ?: throw CoachNotFoundException("Coach bulunamadı: id=$coachExternalId")
        val careers = coachCareerRepository.findAllByCoachExternalIdOrderByStartDateDesc(coachExternalId)
        return coachMapper.toResponse(coach, careers)
    }

    // PUBLIC: bir takımda görev yapmış tüm coach'lar (career'ı o takımı içerenler)
    @Transactional(readOnly = true)
    fun getByTeamExternalId(teamExternalId: Int): List<CoachResponse> {
        val coaches = coachCareerRepository.findAllCareersByTeamExternalId(teamExternalId)

        return coaches
            .groupBy { it.coach }
            .map { (coach, coachCareers) ->
                coach to coachCareers.sortedByDescending { it.startDate }
            }
            .sortedByDescending { (_, coachCareers) ->
                coachCareers
                    .filter { it.teamExternalId == teamExternalId }
                    .maxOf { it.startDate }
            }
            .map { (coach, coachCareers) ->
                coachMapper.toResponse(coach, coachCareers)
            }
    }

    // ADMIN: hard delete (career cascade DB'de)
    @Transactional
    fun deleteByExternalId(coachExternalId: Int) {
        val coach = coachRepository.findByExternalId(coachExternalId)
            ?: throw CoachNotFoundException("Coach bulunamadı: id=$coachExternalId")
        coachRepository.delete(coach)
    }
}