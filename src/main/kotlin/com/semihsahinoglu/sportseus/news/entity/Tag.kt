package com.semihsahinoglu.sportseus.news.entity

import com.semihsahinoglu.sportseus.common.entity.Auditable
import jakarta.persistence.*

@Entity
@Table(
    schema = "news",
    name = "tags",
    uniqueConstraints = [UniqueConstraint(name = "uq_tag_slug", columnNames = ["slug"])]
)
class Tag(
    @Column(nullable = false, length = 100)
    var name: String,

    @Column(nullable = false, length = 120)
    var slug: String
) : Auditable()