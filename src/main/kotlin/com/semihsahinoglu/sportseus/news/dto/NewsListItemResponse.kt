package com.semihsahinoglu.sportseus.news.dto

import com.semihsahinoglu.sportseus.news.entity.NewsCategory
import com.semihsahinoglu.sportseus.news.entity.NewsStatus
import java.time.LocalDateTime
import java.util.UUID

data class NewsListItemResponse(
    val id: UUID,
    val title: String,
    val slug: String,
    val imageUrl: String?,
    val category: NewsCategory,
    val status: NewsStatus,
    val breaking: Boolean,
    val featured: Boolean,
    val publishDate: LocalDateTime?,
    val author: NewsAuthorSummary,
    val tags: List<TagResponse>,
)
