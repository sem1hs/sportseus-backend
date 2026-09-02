package com.semihsahinoglu.sportseus.news.dto

import com.semihsahinoglu.sportseus.news.entity.NewsCategory
import com.semihsahinoglu.sportseus.news.entity.NewsStatus

data class NewsUpdateRequest(
    val title: String? = null,
    val slug: String? = null,
    val content: String? = null,
    val imageUrl: String? = null,
    val category: NewsCategory? = null,
    val status: NewsStatus? = null,
    val breaking: Boolean? = null,
    val featured: Boolean? = null,
    val relations: List<NewsRelationInput>? = null,
    val tags: List<String>? = null,
)
