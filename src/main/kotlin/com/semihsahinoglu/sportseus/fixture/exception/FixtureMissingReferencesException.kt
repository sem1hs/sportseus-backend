package com.semihsahinoglu.sportseus.fixture.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class FixtureMissingReferencesException(
    val missingLeagueExternalIds: List<Int>,
    val missingTeamExternalIds: List<Int>,
) : RuntimeException(
    buildString {
        append("Fixture sync durduruldu — önce eksik referansları oluşturun. ")
        if (missingLeagueExternalIds.isNotEmpty()) append("Eksik league: $missingLeagueExternalIds. ")
        if (missingTeamExternalIds.isNotEmpty()) append("Eksik team: $missingTeamExternalIds.")
    }
)