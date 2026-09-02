package com.semihsahinoglu.sportseus.news.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.news.dto.NewsListItemResponse
import com.semihsahinoglu.sportseus.news.dto.NewsResponse
import com.semihsahinoglu.sportseus.news.entity.NewsCategory
import com.semihsahinoglu.sportseus.news.entity.NewsRelationType
import com.semihsahinoglu.sportseus.news.service.NewsService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/news")
class NewsController(
    private val newsService: NewsService,
) {

    // PUBLIC: yayındaki haberler (sayfalı)
    @GetMapping
    fun getPublished(pageable: Pageable): ResponseEntity<ApiResponse<Page<NewsListItemResponse>>> {
        val news = newsService.getPublished(pageable)
        return ResponseEntity.ok(ApiResponse.success(news))
    }

    // PUBLIC: son dakika
    @GetMapping("/breaking")
    fun getBreaking(pageable: Pageable): ResponseEntity<ApiResponse<Page<NewsListItemResponse>>> {
        val news = newsService.getBreaking(pageable)
        return ResponseEntity.ok(ApiResponse.success(news))
    }

    // PUBLIC: son dakika
    @GetMapping("/featured")
    fun getFeatured(pageable: Pageable): ResponseEntity<ApiResponse<Page<NewsListItemResponse>>> {
        val news = newsService.getFeatured(pageable)
        return ResponseEntity.ok(ApiResponse.success(news))
    }

    // PUBLIC: kategoriye göre
    @GetMapping("/categories/{category}")
    fun getByCategory(
        @PathVariable category: NewsCategory,
        pageable: Pageable,
    ): ResponseEntity<ApiResponse<Page<NewsListItemResponse>>> {
        val news = newsService.getByCategory(category, pageable)
        return ResponseEntity.ok(ApiResponse.success(news))
    }

    // PUBLIC: bir entity'ye bağlı haberler
    @GetMapping("/relations/{type}/{externalId}")
    fun getByRelation(
        @PathVariable type: NewsRelationType,
        @PathVariable externalId: Int,
        pageable: Pageable,
    ): ResponseEntity<ApiResponse<Page<NewsListItemResponse>>> {
        val news = newsService.getByRelation(type, externalId, pageable)
        return ResponseEntity.ok(ApiResponse.success(news))
    }

    // PUBLIC: slug ile tek haber (detay)
    @GetMapping("/{slug}")
    fun getBySlug(@PathVariable slug: String): ResponseEntity<ApiResponse<NewsResponse>> {
        val news = newsService.getBySlug(slug)
        return ResponseEntity.ok(ApiResponse.success(news))
    }
}