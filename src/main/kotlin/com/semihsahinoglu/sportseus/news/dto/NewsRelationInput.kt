package com.semihsahinoglu.sportseus.news.dto

import com.semihsahinoglu.sportseus.news.entity.NewsRelationType

data class NewsRelationInput(
    val type: NewsRelationType,
    val externalId: Int,
    val name: String,
    val imageUrl: String? = null,
)
