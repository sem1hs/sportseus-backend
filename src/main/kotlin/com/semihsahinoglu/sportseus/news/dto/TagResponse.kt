package com.semihsahinoglu.sportseus.news.dto

import java.util.UUID

data class TagResponse(
    val id: UUID,
    val name: String,
    val slug: String,
)
