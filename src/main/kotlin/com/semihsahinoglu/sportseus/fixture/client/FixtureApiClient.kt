package com.semihsahinoglu.sportseus.fixture.client

import com.semihsahinoglu.sportseus.fixture.dto.FixtureApiEnvelope
import com.semihsahinoglu.sportseus.fixture.dto.FixtureApiItem
import com.semihsahinoglu.sportseus.football.exception.FootballApiRateLimitException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class FixtureApiClient(
    @Qualifier("footballRestClient")
    private val footballRestClient: RestClient,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    // /fixtures?league=X&season=Y&team=Z → bir takımın o ligdeki o sezon maçları
    fun fetchByLeagueSeasonTeam(
        leagueExternalId: Int,
        season: Int,
        teamExternalId: Int,
    ): List<FixtureApiItem> {
        log.info(
            "API-Football fixture çağrısı: league={} season={} team={}",
            leagueExternalId, season, teamExternalId
        )

        val envelope = footballRestClient.get()
            .uri { b ->
                b.path("/fixtures")
                    .queryParam("league", leagueExternalId)
                    .queryParam("season", season)
                    .queryParam("team", teamExternalId)
                    .build()
            }
            .retrieve()
            .onStatus({ it.value() == 429 }) { _, _ ->
                throw FootballApiRateLimitException("API-Football limiti aşıldı (429)")
            }
            .body<FixtureApiEnvelope>()

        return envelope?.response ?: emptyList()
    }
}