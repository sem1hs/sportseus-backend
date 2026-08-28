package com.semihsahinoglu.sportseus.coach.dto

data class CoachApiItem(
    val id: Int? = null,
    val name: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val age: Int? = null,
    val birth: CoachBirthNode? = null,
    val nationality: String? = null,
    val height: String? = null,
    val weight: String? = null,
    val photo: String? = null,
    val team: CoachTeamNode? = null,        // "şu anki takım" — career'ın en üstü zaten
    val career: List<CoachCareerNode> = emptyList(),
)