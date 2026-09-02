package com.semihsahinoglu.sportseus.news.repository

import com.semihsahinoglu.sportseus.news.entity.News
import com.semihsahinoglu.sportseus.news.entity.NewsCategory
import com.semihsahinoglu.sportseus.news.entity.NewsRelationType
import com.semihsahinoglu.sportseus.news.entity.NewsStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface NewsRepository : JpaRepository<News, UUID> {
    // slug çakışma kontrolü (create/update)
    fun existsBySlug(slug: String): Boolean

    // PUBLIC: slug ile tek haber (author + relations + tags yüklü)
    @EntityGraph(attributePaths = ["author", "relations", "tags"])
    fun findBySlug(slug: String): News?

    // ADMIN: id ile (ilişkiler yüklü)
    @EntityGraph(attributePaths = ["author", "relations", "tags"])
    fun findWithRelationsById(id: UUID): News?

    // PUBLIC: yayındaki haberler (sayfalı, en yeni önce)
    @EntityGraph(attributePaths = ["author", "tags"])
    fun findAllByStatusOrderByPublishDateDesc(status: NewsStatus, pageable: Pageable): Page<News>

    // PUBLIC: kategoriye göre yayındakiler (sayfalı)
    @EntityGraph(attributePaths = ["author", "tags"])
    fun findAllByStatusAndCategoryOrderByPublishDateDesc(
        status: NewsStatus,
        category: NewsCategory,
        pageable: Pageable,
    ): Page<News>

    // PUBLIC: son dakika (breaking + yayında)
    @EntityGraph(attributePaths = ["author", "tags"])
    fun findAllByStatusAndBreakingTrueOrderByPublishDateDesc(status: NewsStatus, pageable: Pageable): Page<News>

    // PUBLIC: featured + yayında
    @EntityGraph(attributePaths = ["author", "tags"])
    fun findAllByStatusAndFeaturedTrueOrderByPublishDateDesc(status: NewsStatus, pageable: Pageable): Page<News>

    // PUBLIC: bir entity'ye bağlı haberler (snapshot üzerinden — "Trabzonspor haberleri")
    @EntityGraph(attributePaths = ["author", "tags"])
    @Query(
        """
        SELECT DISTINCT n FROM News n
        JOIN n.relations r
        WHERE r.type = :type AND r.externalId = :externalId
          AND n.status = :status
        ORDER BY n.publishDate DESC
    """
    )
    fun findByRelation(
        @Param("type") type: NewsRelationType,
        @Param("externalId") externalId: Int,
        @Param("status") status: NewsStatus,
        pageable: Pageable,
    ): Page<News>

    // ADMIN: tüm haberler durum filtresiyle (taslak dahil, sayfalı)
    @EntityGraph(attributePaths = ["author", "tags"])
    fun findAllByOrderByCreatedDateDesc(pageable: Pageable): Page<News>
}