package com.semihsahinoglu.sportseus.team.mapper

import com.semihsahinoglu.sportseus.team.dto.statistics.BiggestGoals
import com.semihsahinoglu.sportseus.team.dto.statistics.GoalDetail
import com.semihsahinoglu.sportseus.team.dto.statistics.HomeAway
import com.semihsahinoglu.sportseus.team.dto.statistics.HomeAwayStr
import com.semihsahinoglu.sportseus.team.dto.statistics.HomeAwayTotal
import com.semihsahinoglu.sportseus.team.dto.statistics.HomeAwayTotalStr
import com.semihsahinoglu.sportseus.team.dto.statistics.MinuteBucket
import com.semihsahinoglu.sportseus.team.dto.statistics.StatBiggest
import com.semihsahinoglu.sportseus.team.dto.statistics.StatCards
import com.semihsahinoglu.sportseus.team.dto.statistics.StatFixtures
import com.semihsahinoglu.sportseus.team.dto.statistics.StatGoals
import com.semihsahinoglu.sportseus.team.dto.statistics.StatPenalty
import com.semihsahinoglu.sportseus.team.dto.statistics.Streak
import com.semihsahinoglu.sportseus.team.dto.statistics.TeamStatisticsNode
import com.semihsahinoglu.sportseus.team.dto.statistics.TotalPercentage
import com.semihsahinoglu.sportseus.team.dto.statistics.UnderOver
import org.springframework.stereotype.Component

@Component
class TeamStatisticsMerger {

    fun merge(existing: TeamStatisticsNode, incoming: TeamStatisticsNode): TeamStatisticsNode =
        TeamStatisticsNode(
            form = incoming.form ?: existing.form,
            fixtures = existing.fixtures.mergeWith(incoming.fixtures),
            goals = existing.goals.mergeWith(incoming.goals),
            biggest = existing.biggest.mergeWith(incoming.biggest),
            cleanSheet = existing.cleanSheet.mergeWith(incoming.cleanSheet),
            failedToScore = existing.failedToScore.mergeWith(incoming.failedToScore),
            penalty = existing.penalty.mergeWith(incoming.penalty),
            lineups = incoming.lineups.ifEmpty { existing.lineups },   // liste → replace
            cards = existing.cards.mergeWith(incoming.cards),
        )

    private fun Map<String, MinuteBucket>.mergeMinute(incoming: Map<String, MinuteBucket>): Map<String, MinuteBucket> {
        if (incoming.isEmpty()) return this
        val result = this.toMutableMap()
        incoming.forEach { (key, inc) -> result[key] = result[key].mergeWith(inc) }
        return result
    }

    private fun Map<String, UnderOver>.mergeUnderOver(incoming: Map<String, UnderOver>): Map<String, UnderOver> {
        if (incoming.isEmpty()) return this
        val result = this.toMutableMap()
        incoming.forEach { (key, inc) -> result[key] = result[key].mergeWith(inc) }
        return result
    }

    private fun StatFixtures?.mergeWith(incoming: StatFixtures?): StatFixtures? {
        if (incoming == null) return this
        val base = this ?: StatFixtures()
        return StatFixtures(
            played = base.played.mergeWith(incoming.played),
            wins = base.wins.mergeWith(incoming.wins),
            draws = base.draws.mergeWith(incoming.draws),
            loses = base.loses.mergeWith(incoming.loses),
        )
    }

    private fun StatGoals?.mergeWith(incoming: StatGoals?): StatGoals? {
        if (incoming == null) return this
        val base = this ?: StatGoals()
        return StatGoals(
            goalsFor = base.goalsFor.mergeWith(incoming.goalsFor),
            against = base.against.mergeWith(incoming.against),
        )
    }

    private fun GoalDetail?.mergeWith(incoming: GoalDetail?): GoalDetail? {
        if (incoming == null) return this
        val base = this ?: GoalDetail()
        return GoalDetail(
            total = base.total.mergeWith(incoming.total),
            average = base.average.mergeWith(incoming.average),
            minute = base.minute.mergeMinute(incoming.minute),
            underOver = base.underOver.mergeUnderOver(incoming.underOver),
        )
    }

    private fun StatBiggest?.mergeWith(incoming: StatBiggest?): StatBiggest? {
        if (incoming == null) return this
        val base = this ?: StatBiggest()
        return StatBiggest(
            streak = base.streak.mergeWith(incoming.streak),
            wins = base.wins.mergeWith(incoming.wins),
            loses = base.loses.mergeWith(incoming.loses),
            goals = base.goals.mergeWith(incoming.goals),
        )
    }

    private fun BiggestGoals?.mergeWith(incoming: BiggestGoals?): BiggestGoals? {
        if (incoming == null) return this
        val base = this ?: BiggestGoals()
        return BiggestGoals(
            goalsFor = base.goalsFor.mergeWith(incoming.goalsFor),
            against = base.against.mergeWith(incoming.against),
        )
    }

    private fun StatPenalty?.mergeWith(incoming: StatPenalty?): StatPenalty? {
        if (incoming == null) return this
        val base = this ?: StatPenalty()
        return StatPenalty(
            scored = base.scored.mergeWith(incoming.scored),
            missed = base.missed.mergeWith(incoming.missed),
            total = incoming.total ?: base.total,
        )
    }

    private fun StatCards?.mergeWith(incoming: StatCards?): StatCards? {
        if (incoming == null) return this
        val base = this ?: StatCards()
        return StatCards(
            yellow = base.yellow.mergeMinute(incoming.yellow),
            red = base.red.mergeMinute(incoming.red),
        )
    }

    private fun HomeAway?.mergeWith(incoming: HomeAway?): HomeAway? {
        if (incoming == null) return this
        val base = this ?: HomeAway()
        return HomeAway(
            home = incoming.home ?: base.home,
            away = incoming.away ?: base.away,
        )
    }

    private fun HomeAwayStr?.mergeWith(incoming: HomeAwayStr?): HomeAwayStr? {
        if (incoming == null) return this
        val base = this ?: HomeAwayStr()
        return HomeAwayStr(
            home = incoming.home ?: base.home,
            away = incoming.away ?: base.away,
        )
    }

    private fun HomeAwayTotal?.mergeWith(incoming: HomeAwayTotal?): HomeAwayTotal? {
        if (incoming == null) return this
        val base = this ?: HomeAwayTotal()
        return HomeAwayTotal(
            home = incoming.home ?: base.home,
            away = incoming.away ?: base.away,
            total = incoming.total ?: base.total,
        )
    }

    private fun HomeAwayTotalStr?.mergeWith(incoming: HomeAwayTotalStr?): HomeAwayTotalStr? {
        if (incoming == null) return this
        val base = this ?: HomeAwayTotalStr()
        return HomeAwayTotalStr(
            home = incoming.home ?: base.home,
            away = incoming.away ?: base.away,
            total = incoming.total ?: base.total,
        )
    }

    private fun MinuteBucket?.mergeWith(incoming: MinuteBucket?): MinuteBucket {
        if (incoming == null) return this ?: MinuteBucket()
        val base = this ?: MinuteBucket()
        return MinuteBucket(
            total = incoming.total ?: base.total,
            percentage = incoming.percentage ?: base.percentage,
        )
    }

    private fun UnderOver?.mergeWith(incoming: UnderOver?): UnderOver {
        if (incoming == null) return this ?: UnderOver()
        val base = this ?: UnderOver()
        return UnderOver(
            over = incoming.over ?: base.over,
            under = incoming.under ?: base.under,
        )
    }

    private fun TotalPercentage?.mergeWith(incoming: TotalPercentage?): TotalPercentage? {
        if (incoming == null) return this
        val base = this ?: TotalPercentage()
        return TotalPercentage(
            total = incoming.total ?: base.total,
            percentage = incoming.percentage ?: base.percentage,
        )
    }

    private fun Streak?.mergeWith(incoming: Streak?): Streak? {
        if (incoming == null) return this
        val base = this ?: Streak()
        return Streak(
            wins = incoming.wins ?: base.wins,
            draws = incoming.draws ?: base.draws,
            loses = incoming.loses ?: base.loses,
        )
    }
}