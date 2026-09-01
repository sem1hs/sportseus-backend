package com.semihsahinoglu.sportseus.news.dto

import com.semihsahinoglu.sportseus.news.entity.NewsCategory
import com.semihsahinoglu.sportseus.news.entity.NewsStatus
import java.time.LocalDateTime
import java.util.UUID

data class NewsResponse(
    val id: UUID,
    val title: String,
    val slug: String,
    val content: String,
    val imageUrl: String?,
    val category: NewsCategory,
    val status: NewsStatus,
    val breaking: Boolean,
    val publishDate: LocalDateTime?,
    val author: NewsAuthorSummary,
    val relations: List<NewsRelationResponse>,
    val tags: List<TagResponse>,
)
