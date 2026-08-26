package com.semihsahinoglu.sportseus.league.client

import com.semihsahinoglu.sportseus.football.exception.FootballApiRateLimitException
import com.semihsahinoglu.sportseus.league.dto.LeagueApiEnvelope
import com.semihsahinoglu.sportseus.league.dto.LeagueApiItem
import com.semihsahinoglu.sportseus.league.dto.LeagueByTeamApiEnvelope
import com.semihsahinoglu.sportseus.league.dto.LeagueByTeamApiItem
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class LeagueApiClient(
    @Qualifier("footballRestClient")
    private val footballRestClient: RestClient,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    // /leagues?id=X&season=Y → tek lig (yoksa null)
    fun fetchLeague(externalId: Int, season: Int): LeagueApiItem? {
        log.info("API-Football lig çağrısı: id={} season={}", externalId, season)

        val envelope = footballRestClient.get()
            .uri { b ->
                b.path("/leagues")
                    .queryParam("id", externalId)
                    .queryParam("season", season)
                    .build()
            }
            .retrieve()
            .onStatus({ it.value() == 429 }) { _, _ ->
                throw FootballApiRateLimitException("API-Football limiti aşıldı (429)")
            }
            .body(LeagueApiEnvelope::class.java)

        // response dizisi boşsa o lig/sezon planında yok demektir
        return envelope?.response?.firstOrNull()
    }

    // /leagues?team=X → takımın tüm ligleri (sezonlarıyla)
    fun fetchLeaguesByTeam(teamExternalId: Int): List<LeagueByTeamApiItem> {
        log.info("API-Football takıma göre lig çağrısı: team={}", teamExternalId)
        val envelope = footballRestClient.get()
            .uri { b -> b.path("/leagues").queryParam("team", teamExternalId).build() }
            .retrieve()
            .onStatus({ it.value() == 429 }) { _, _ ->
                throw FootballApiRateLimitException("API-Football limiti aşıldı (429)")
            }
            .body(LeagueByTeamApiEnvelope::class.java)
        return envelope?.response ?: emptyList()
    }
}