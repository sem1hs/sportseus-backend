package com.semihsahinoglu.sportseus.news.mapper

import com.semihsahinoglu.sportseus.news.dto.NewsAuthorSummary
import com.semihsahinoglu.sportseus.news.dto.NewsCreateRequest
import com.semihsahinoglu.sportseus.news.dto.NewsListItemResponse
import com.semihsahinoglu.sportseus.news.dto.NewsRelationInput
import com.semihsahinoglu.sportseus.news.dto.NewsRelationResponse
import com.semihsahinoglu.sportseus.news.dto.NewsResponse
import com.semihsahinoglu.sportseus.news.dto.TagResponse
import com.semihsahinoglu.sportseus.news.entity.News
import com.semihsahinoglu.sportseus.news.entity.NewsRelation
import com.semihsahinoglu.sportseus.news.entity.NewsStatus
import com.semihsahinoglu.sportseus.news.entity.Tag
import com.semihsahinoglu.sportseus.user.entity.User
import org.springframework.stereotype.Component

@Component
class NewsMapper {

    fun toEntity(request: NewsCreateRequest, author: User): News = News(
        title = request.title,
        slug = request.slug,
        content = request.content,
        imageUrl = request.imageUrl,
        category = request.category,
        status = NewsStatus.DRAFT,
        breaking = request.breaking,
        author = author,
    )

    fun toRelationEntity(news: News, input: NewsRelationInput): NewsRelation =
        NewsRelation(
            news = news,
            type = input.type,
            externalId = input.externalId,
            name = input.name,
            imageUrl = input.imageUrl,
        )

    fun toResponse(news: News): NewsResponse =
        NewsResponse(
            id = news.id!!,
            title = news.title,
            slug = news.slug,
            content = news.content,
            imageUrl = news.imageUrl,
            category = news.category,
            status = news.status,
            breaking = news.breaking,
            publishDate = news.publishDate,
            author = toAuthorSummary(news),
            relations = news.relations.map(::toRelationResponse),
            tags = news.tags.map(::toTagResponse).sortedBy { it.name },
        )

    fun toListItem(news: News): NewsListItemResponse =
        NewsListItemResponse(
            id = news.id!!,
            title = news.title,
            slug = news.slug,
            imageUrl = news.imageUrl,
            category = news.category,
            status = news.status,
            breaking = news.breaking,
            publishDate = news.publishDate,
            author = toAuthorSummary(news),
            tags = news.tags.map(::toTagResponse).sortedBy { it.name },
        )

    fun toTagResponse(tag: Tag): TagResponse =
        TagResponse(id = tag.id!!, name = tag.name, slug = tag.slug)

    private fun toRelationResponse(r: NewsRelation): NewsRelationResponse =
        NewsRelationResponse(
            id = r.id!!,
            type = r.type,
            externalId = r.externalId,
            name = r.name,
            imageUrl = r.imageUrl,
        )

    private fun toAuthorSummary(news: News): NewsAuthorSummary =
        NewsAuthorSummary(
            id = news.author.id!!,
            displayName = news.author.displayName,
            email = news.author.email,
        )
}