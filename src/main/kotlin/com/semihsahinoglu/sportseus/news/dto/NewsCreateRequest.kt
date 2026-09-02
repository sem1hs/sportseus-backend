package com.semihsahinoglu.sportseus.news.dto

import com.semihsahinoglu.sportseus.news.entity.NewsCategory
import com.semihsahinoglu.sportseus.news.entity.NewsStatus

data class NewsCreateRequest(
    val title: String,
    val slug: String,
    val content: String,
    val imageUrl: String? = null,
    val category: NewsCategory,
    val status: NewsStatus = NewsStatus.DRAFT,
    val breaking: Boolean = false,
    val featured: Boolean = false,
    val relations: List<NewsRelationInput> = emptyList(),
    val tags: List<String> = emptyList(),
)
