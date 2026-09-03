package com.semihsahinoglu.sportseus.news.entity

import com.semihsahinoglu.sportseus.common.entity.Auditable
import com.semihsahinoglu.sportseus.user.entity.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    schema = "news",
    name = "news",
    uniqueConstraints = [UniqueConstraint(name = "uq_news_slug", columnNames = ["slug"])]
)
class News(
    @Column(nullable = false, length = 255)
    var title: String,

    @Column(nullable = false, length = 300)
    var slug: String,

    @Column(nullable = false, columnDefinition = "text")
    var content: String,

    @Column(name = "image_url", columnDefinition = "text")
    var imageUrl: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    var category: NewsCategory,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: NewsStatus = NewsStatus.DRAFT,

    @Column(nullable = false)
    var breaking: Boolean = false,

    @Column(name = "publish_date")
    var publishDate: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    var author: User,

    @OneToMany(mappedBy = "news", cascade = [CascadeType.ALL], orphanRemoval = true)
    var relations: MutableList<NewsRelation> = mutableListOf(),

    @ManyToMany
    @JoinTable(
        schema = "news",
        name = "news_tags",
        joinColumns = [JoinColumn(name = "news_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")],
    )
    var tags: MutableSet<Tag> = mutableSetOf(),

    @Column(nullable = false)
    var featured: Boolean = false

) : Auditable() {
    fun changeStatus(newStatus: NewsStatus) {
        if (newStatus == NewsStatus.PUBLISHED && this.publishDate == null) this.publishDate = LocalDateTime.now()
        this.status = newStatus
    }

    fun applyUpdate(
        title: String?,
        content: String?,
        imageUrl: String?,
        category: NewsCategory?,
        breaking: Boolean?,
        status: NewsStatus?,
        featured: Boolean?,
    ) {
        title?.let { this.title = it }
        content?.let { this.content = it }
        imageUrl?.let { this.imageUrl = it }
        category?.let { this.category = it }
        breaking?.let { this.breaking = it }
        status?.let { this.changeStatus(it) }
        featured?.let { this.featured = it }
    }

    fun replaceRelations(newRelations: List<NewsRelation>) {
        this.relations.clear()
        newRelations.forEach { it.news = this }
        this.relations.addAll(newRelations)
    }
}