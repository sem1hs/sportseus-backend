package com.semihsahinoglu.sportseus.team.client

import com.semihsahinoglu.sportseus.football.exception.FootballApiRateLimitException
import com.semihsahinoglu.sportseus.team.dto.TeamApiEnvelope
import com.semihsahinoglu.sportseus.team.dto.TeamApiItem
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class TeamApiClient(
    @Qualifier("footballRestClient")
    private val footballRestClient: RestClient,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    // Ligin tüm takımları: /teams?league=X&season=Y
    fun fetchTeamsByLeague(leagueExternalId: Int, season: Int): List<TeamApiItem> {
        log.info("API-Football takım çağrısı: league={} season={}", leagueExternalId, season)
        return footballRestClient.get()
            .uri { b ->
                b.path("/teams")
                    .queryParam("league", leagueExternalId)
                    .queryParam("season", season)
                    .build()
            }
            .retrieve()
            .onStatus({ it.value() == 429 }) { _, _ ->
                throw FootballApiRateLimitException("API-Football limiti aşıldı (429)")
            }
            .body(TeamApiEnvelope::class.java)?.response ?: emptyList()
    }

    // Tek takım: /teams?id=Z&league=X&season=Y (API lig+season'ı zorunlu kılıyor)
    fun fetchTeam(teamExternalId: Int, leagueExternalId: Int, season: Int): TeamApiItem? {
        log.info("API-Football tek takım: id={} league={} season={}", teamExternalId, leagueExternalId, season)
        return footballRestClient.get()
            .uri { b ->
                b.path("/teams")
                    .queryParam("id", teamExternalId)
                    .queryParam("league", leagueExternalId)
                    .queryParam("season", season)
                    .build()
            }
            .retrieve()
            .onStatus({ it.value() == 429 }) { _, _ ->
                throw FootballApiRateLimitException("API-Football limiti aşıldı (429)")
            }
            .body(TeamApiEnvelope::class.java)
            ?.response?.firstOrNull()
    }
}