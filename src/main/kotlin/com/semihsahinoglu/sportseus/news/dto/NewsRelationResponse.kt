package com.semihsahinoglu.sportseus.news.dto

import com.semihsahinoglu.sportseus.news.entity.NewsRelationType
import java.util.UUID

data class NewsRelationResponse(
    val id: UUID,
    val type: NewsRelationType,
    val externalId: Int,
    val name: String,
    val imageUrl: String?,
)
