package com.semihsahinoglu.sportseus.news.dto

import java.util.UUID

data class NewsAuthorSummary(
    val id: UUID,
    val displayName: String?,
    val email: String?,
)
