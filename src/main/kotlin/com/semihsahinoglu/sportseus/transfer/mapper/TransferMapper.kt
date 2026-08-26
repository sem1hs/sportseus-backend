package com.semihsahinoglu.sportseus.transfer.mapper

import com.semihsahinoglu.sportseus.player.entity.Player
import com.semihsahinoglu.sportseus.team.entity.Team
import com.semihsahinoglu.sportseus.transfer.dto.TransferPlayerSummary
import com.semihsahinoglu.sportseus.transfer.dto.TransferResponse
import com.semihsahinoglu.sportseus.transfer.dto.TransferTeamSummary
import com.semihsahinoglu.sportseus.transfer.entity.Transfer
import com.semihsahinoglu.sportseus.transfer.entity.TransferType
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDate

@Component
class TransferMapper {

    fun toEntity(
        player: Player,
        teamIn: Team,
        teamOut: Team,
        date: LocalDate,
        rawType: String,
    ): Transfer {
        val (type, fee) = parseType(rawType)
        return Transfer(
            player = player,
            teamIn = teamIn,
            teamOut = teamOut,
            date = date,
            rawType = rawType,
            transferType = type,
            fee = fee,
        )
    }

    fun applyApiData(target: Transfer, teamIn: Team, teamOut: Team, rawType: String) {
        val (type, fee) = parseType(rawType)
        target.teamIn = teamIn
        target.teamOut = teamOut
        target.rawType = rawType
        target.transferType = type
        target.fee = fee
    }

    fun toResponse(t: Transfer): TransferResponse =
        TransferResponse(
            id = t.id!!,
            date = t.date,
            rawType = t.rawType,
            transferType = t.transferType,
            fee = t.fee,
            manuallyEdited = t.manuallyEdited,
            player = TransferPlayerSummary(
                id = t.player.id!!,
                externalId = t.player.externalId,
                name = t.player.name,
                photo = t.player.photo,
            ),
            teamIn = TransferTeamSummary(
                id = t.teamIn.id!!,
                externalId = t.teamIn.externalId.toLong(),
                name = t.teamIn.name,
                logoUrl = t.teamIn.logoUrl,
            ),
            teamOut = TransferTeamSummary(
                id = t.teamOut.id!!,
                externalId = t.teamOut.externalId.toLong(),
                name = t.teamOut.name,
                logoUrl = t.teamOut.logoUrl,
            ),
        )

    fun parseType(raw: String): Pair<TransferType, Long?> {
        val trimmed = raw.trim()
        val lower = trimmed.lowercase()
        val fee = parseFee(trimmed)

        return when {
            lower == "loan" -> TransferType.LOAN to null
            lower == "free agent" || lower == "free" -> TransferType.FREE to null
            fee != null -> TransferType.SALE to fee
            else -> TransferType.UNKNOWN to null
        }
    }

    private fun parseFee(raw: String): Long? {
        val cleaned = raw.replace("€", "").replace(" ", "").trim()
        if (cleaned.isEmpty()) return null

        val multiplier = when (cleaned.last().uppercaseChar()) {
            'M' -> 1_000_000L
            'K' -> 1_000L
            else -> return null
        }

        val numberPart = cleaned.dropLast(1)
        val value = numberPart.toBigDecimalOrNull() ?: return null

        return value.multiply(BigDecimal(multiplier)).toLong()
    }
}