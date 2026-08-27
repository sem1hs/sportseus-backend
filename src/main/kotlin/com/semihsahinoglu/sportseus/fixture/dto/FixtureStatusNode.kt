package com.semihsahinoglu.sportseus.fixture.dto

data class FixtureStatusNode(
    val long: String? = null,      // "Match Finished"
    val short: String? = null,     // "FT"
    val elapsed: Int? = null,      // 90
    val extra: Int? = null
)
