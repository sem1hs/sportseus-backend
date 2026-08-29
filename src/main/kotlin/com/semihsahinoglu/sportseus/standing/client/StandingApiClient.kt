package com.semihsahinoglu.sportseus.standing.client

import com.semihsahinoglu.sportseus.football.exception.FootballApiRateLimitException
import com.semihsahinoglu.sportseus.standing.dto.StandingApiEnvelope
import com.semihsahinoglu.sportseus.standing.dto.StandingApiItem
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class StandingApiClient(
    @Qualifier("footballRestClient")
    private val footballRestClient: RestClient,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    // /standings?league=X&season=Y → ligin puan tablosu (tek item, yoksa null)
    fun fetchByLeagueAndSeason(leagueExternalId: Int, season: Int): StandingApiItem? {
        log.info("API-Football standing çağrısı: league={} season={}", leagueExternalId, season)

        val envelope = footballRestClient.get()
            .uri { b ->
                b.path("/standings")
                    .queryParam("league", leagueExternalId)
                    .queryParam("season", season)
                    .build()
            }
            .retrieve()
            .onStatus({ it.value() == 429 }) { _, _ ->
                throw FootballApiRateLimitException("API-Football limiti aşıldı (429)")
            }
            .body<StandingApiEnvelope>()

        return envelope?.response?.firstOrNull()
    }
}