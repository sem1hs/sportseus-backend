package com.semihsahinoglu.sportseus.coach.mapper

import com.semihsahinoglu.sportseus.coach.dto.CoachApiItem
import com.semihsahinoglu.sportseus.coach.dto.CoachCareerInput
import com.semihsahinoglu.sportseus.coach.dto.CoachCareerNode
import com.semihsahinoglu.sportseus.coach.dto.CoachCareerResponse
import com.semihsahinoglu.sportseus.coach.dto.CoachCreateRequest
import com.semihsahinoglu.sportseus.coach.dto.CoachResponse
import com.semihsahinoglu.sportseus.coach.entity.Coach
import com.semihsahinoglu.sportseus.coach.entity.CoachCareer
import org.springframework.stereotype.Component

@Component
class CoachMapper {

    fun toEntity(item: CoachApiItem): Coach =
        Coach(
            externalId = requireNotNull(item.id) { "Coach id null olamaz" },
            name = item.name ?: "",
            firstName = item.firstname,
            lastName = item.lastname,
            age = item.age,
            birthDate = item.birth?.date,
            birthPlace = item.birth?.place,
            birthCountry = item.birth?.country,
            nationality = item.nationality,
            height = item.height,
            weight = item.weight,
            photo = item.photo,
        )

    fun toCareerEntity(coach: Coach, node: CoachCareerNode): CoachCareer =
        CoachCareer(
            coach = coach,
            teamExternalId = requireNotNull(node.team?.id) { "Career team id null olamaz" },
            teamName = node.team?.name ?: "",
            teamLogo = node.team?.logo,
            startDate = requireNotNull(node.start) { "Career start null olamaz" },
            endDate = node.end,
        )

    fun toManualEntity(request: CoachCreateRequest): Coach =
        Coach(
            externalId = null,                  // elle eklemede API id yok
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
            manuallyEdited = false,
            manualAdded = true,
        )

    fun toManualCareerEntity(coach: Coach, input: CoachCareerInput): CoachCareer =
        CoachCareer(
            coach = coach,
            teamExternalId = input.teamExternalId,
            teamName = input.teamName,
            teamLogo = input.teamLogo,
            startDate = input.startDate,
            endDate = input.endDate,
            manualAdded = true,
            manuallyEdited = false,
        )

    fun applyApiData(target: Coach, item: CoachApiItem) {
        item.name?.let { target.name = it }       // name null gelirse mevcut adı koru
        target.firstName = item.firstname
        target.lastName = item.lastname
        target.age = item.age
        target.birthDate = item.birth?.date
        target.birthPlace = item.birth?.place
        target.birthCountry = item.birth?.country
        target.nationality = item.nationality
        target.height = item.height
        target.weight = item.weight
        target.photo = item.photo
    }

    fun applyCareerData(target: CoachCareer, node: CoachCareerNode) {
        node.team?.name?.let { target.teamName = it }
        target.teamLogo = node.team?.logo
        target.endDate = node.end             // görevden ayrılınca null → tarih
    }

    fun toResponse(coach: Coach, careers: List<CoachCareer>): CoachResponse =
        CoachResponse(
            id = coach.id!!,
            externalId = coach.externalId,
            name = coach.name,
            firstName = coach.firstName,
            lastName = coach.lastName,
            age = coach.age,
            birthDate = coach.birthDate,
            birthPlace = coach.birthPlace,
            birthCountry = coach.birthCountry,
            nationality = coach.nationality,
            height = coach.height,
            weight = coach.weight,
            photo = coach.photo,
            manuallyEdited = coach.manuallyEdited,
            career = careers.map(::toCareerResponse),
        )

    fun toCareerResponse(c: CoachCareer): CoachCareerResponse =
        CoachCareerResponse(
            id = c.id!!,
            teamExternalId = c.teamExternalId,
            teamName = c.teamName,
            teamLogo = c.teamLogo,
            startDate = c.startDate,
            endDate = c.endDate,
        )
}