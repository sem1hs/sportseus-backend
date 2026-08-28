package com.semihsahinoglu.sportseus.lineup.dto

import java.util.UUID

data class LineupResponse(
    val id: UUID,
    val formation: String?,
    val manuallyEdited: Boolean?,
    val team: LineupTeamSummary,
    val coach: LineupCoachSummary?,        // snapshot, null olabilir
    val startXI: List<LineupPlayerItem>,   // isStarter = true
    val substitutes: List<LineupPlayerItem>, // isStarter = false
)