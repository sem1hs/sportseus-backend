package com.semihsahinoglu.sportseus.fixture.dto

data class FixtureTeamNode(
    val id: Int? = null,           // team.teams'e çözülecek (katı)
    val name: String? = null,
    val winner: Boolean? = null
)
