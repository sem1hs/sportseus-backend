package com.semihsahinoglu.sportseus.lineup.service

import com.semihsahinoglu.sportseus.fixture.service.FixtureService
import com.semihsahinoglu.sportseus.lineup.client.LineupApiClient
import com.semihsahinoglu.sportseus.lineup.dto.LineupResponse
import org.springframework.stereotype.Service

@Service
class LineupFixtureSyncService(
    private val lineupApiClient: LineupApiClient,
    private val lineupService: LineupService,
    private val fixtureService: FixtureService,
) {
    // ADMIN: maçın dizilişlerini sync (/fixtures/lineups?fixture=) — lineup-başı izolasyon
    fun syncByFixture(fixtureExternalId: Long): List<LineupResponse> {
        // KATI: fixture DB'de olmalı (yoksa hata, sync başlamaz)
        val fixture = fixtureService.getByExternalIdEntity(fixtureExternalId)
        val items = lineupApiClient.fetchByFixture(fixtureExternalId)

        return items.mapNotNull { item ->
            try {
                lineupService.syncOneLineup(fixture, item)   // farklı bean → proxy → ayrı tx
            } catch (e: Exception) {
                null
            }
        }
    }
}