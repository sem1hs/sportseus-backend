package com.semihsahinoglu.sportseus.team.entity

import com.semihsahinoglu.sportseus.common.entity.Auditable
import com.semihsahinoglu.sportseus.league.entity.League
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(schema = "team", name = "team_statistics")
class TeamStatistics(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    var team: Team,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "league_id", nullable = false)
    var league: League,

    @Column(nullable = false)
    var season: Int,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "stats", columnDefinition = "jsonb", nullable = false)
    var stats: String

) : Auditable()