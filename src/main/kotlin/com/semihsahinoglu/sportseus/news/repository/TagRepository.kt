package com.semihsahinoglu.sportseus.news.repository

import com.semihsahinoglu.sportseus.news.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TagRepository : JpaRepository<Tag, UUID> {
    // get-or-create için — slug ile bul
    fun findBySlug(slug: String): Tag?

    fun existsBySlug(slug: String): Boolean

    fun findTop5ByOrderByCreatedDateDesc(): List<Tag>
}