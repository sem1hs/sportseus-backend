package com.semihsahinoglu.sportseus.player.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class MissingReferencesException(
    val missingTeamExternalIds: List<Long>,
    val missingLeagueExternalIds: List<Long>,
) : RuntimeException(
    buildString {
        append("Sync durduruldu — önce eksik referansları oluşturun. ")
        if (missingTeamExternalIds.isNotEmpty()) append("Eksik team: $missingTeamExternalIds. ")
        if (missingLeagueExternalIds.isNotEmpty()) append("Eksik league: $missingLeagueExternalIds.")
    }
)