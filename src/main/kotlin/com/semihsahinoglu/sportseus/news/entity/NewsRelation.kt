package com.semihsahinoglu.sportseus.news.entity

import com.semihsahinoglu.sportseus.common.entity.Auditable
import jakarta.persistence.*

@Entity
@Table(schema = "news", name = "news_relations")
class NewsRelation(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    var news: News,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var type: NewsRelationType,

    @Column(name = "external_id", nullable = false)
    var externalId: Int,

    @Column(nullable = false, length = 200)
    var name: String,

    @Column(name = "image_url", columnDefinition = "text")
    var imageUrl: String? = null,
) : Auditable()