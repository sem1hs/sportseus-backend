package com.semihsahinoglu.sportseus.team.client

import com.semihsahinoglu.sportseus.football.exception.FootballApiRateLimitException
import com.semihsahinoglu.sportseus.team.dto.statistics.TeamStatisticsEnvelope
import com.semihsahinoglu.sportseus.team.dto.statistics.TeamStatisticsNode
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class TeamStatisticsApiClient(
    @Qualifier("footballRestClient")
    private val footballRestClient: RestClient,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    // /teams/statistics?league=X&season=Y&team=Z → tek nesne (response bir object, dizi değil)
    fun fetchStatistics(teamExternalId: Int, leagueExternalId: Int, season: Int): TeamStatisticsNode? {
        log.info("İstatistik çağrısı: team={} league={} season={}", teamExternalId, leagueExternalId, season)
        return footballRestClient.get()
            .uri { b ->
                b.path("/teams/statistics")
                    .queryParam("team", teamExternalId)
                    .queryParam("league", leagueExternalId)
                    .queryParam("season", season)
                    .build()
            }
            .retrieve()
            .onStatus({ it.value() == 429 }) { _, _ ->
                throw FootballApiRateLimitException("API-Football limiti aşıldı (429)")
            }
            .body(TeamStatisticsEnvelope::class.java)
            ?.response
    }

}