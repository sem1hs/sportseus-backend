package com.semihsahinoglu.sportseus.lineup.client

import com.semihsahinoglu.sportseus.football.exception.FootballApiRateLimitException
import com.semihsahinoglu.sportseus.lineup.dto.LineupApiEnvelope
import com.semihsahinoglu.sportseus.lineup.dto.LineupApiItem
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class LineupApiClient(
    @Qualifier("footballRestClient")
    private val footballRestClient: RestClient,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    // /fixtures/lineups?fixture=X → iki takımın dizilişi (home + away)
    fun fetchByFixture(fixtureExternalId: Long): List<LineupApiItem> {
        log.info("API-Football lineup çağrısı: fixture={}", fixtureExternalId)

        val envelope = footballRestClient.get()
            .uri { b ->
                b.path("/fixtures/lineups")
                    .queryParam("fixture", fixtureExternalId)
                    .build()
            }
            .retrieve()
            .onStatus({ it.value() == 429 }) { _, _ ->
                throw FootballApiRateLimitException("API-Football limiti aşıldı (429)")
            }
            .body(LineupApiEnvelope::class.java)

        return envelope?.response ?: emptyList()
    }
}