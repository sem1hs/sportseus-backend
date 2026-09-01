package com.semihsahinoglu.sportseus.news.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.news.dto.NewsCreateRequest
import com.semihsahinoglu.sportseus.news.dto.NewsListItemResponse
import com.semihsahinoglu.sportseus.news.dto.NewsResponse
import com.semihsahinoglu.sportseus.news.dto.NewsUpdateRequest
import com.semihsahinoglu.sportseus.news.service.NewsService
import com.semihsahinoglu.sportseus.security.entity.UserPrincipal
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/admin/news")
class NewsAdminController(
    private val newsService: NewsService,
) {
    // ADMIN: haber oluştur (author JWT'den)
    @PostMapping
    fun create(
        @AuthenticationPrincipal principal: UserPrincipal,
        @RequestBody request: NewsCreateRequest,
    ): ResponseEntity<ApiResponse<NewsResponse>> {
        val news = newsService.create(request, principal.user)
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(news))
    }

    // ADMIN: güncelle (partial)
    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @RequestBody request: NewsUpdateRequest,
    ): ResponseEntity<ApiResponse<NewsResponse>> {
        val news = newsService.update(id, request)
        return ResponseEntity.ok(ApiResponse.success(news))
    }

    // ADMIN: tüm haberler (taslak dahil, sayfalı)
    @GetMapping
    fun getAll(pageable: Pageable): ResponseEntity<ApiResponse<Page<NewsListItemResponse>>> {
        val news = newsService.getAllForAdmin(pageable)
        return ResponseEntity.ok(ApiResponse.success(news))
    }

    // ADMIN: tek haber detay (taslak dahil — önizleme)
    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<ApiResponse<NewsResponse>> {
        val news = newsService.getByIdForAdmin(id)
        return ResponseEntity.ok(ApiResponse.success(news))
    }

    // ADMIN: sil
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        newsService.delete(id)
        return ResponseEntity.noContent().build()
    }
}