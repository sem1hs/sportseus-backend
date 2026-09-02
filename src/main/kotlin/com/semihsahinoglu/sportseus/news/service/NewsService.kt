package com.semihsahinoglu.sportseus.news.service

import com.semihsahinoglu.sportseus.news.dto.NewsCreateRequest
import com.semihsahinoglu.sportseus.news.dto.NewsListItemResponse
import com.semihsahinoglu.sportseus.news.dto.NewsResponse
import com.semihsahinoglu.sportseus.news.dto.NewsUpdateRequest
import com.semihsahinoglu.sportseus.news.entity.NewsCategory
import com.semihsahinoglu.sportseus.news.entity.NewsRelationType
import com.semihsahinoglu.sportseus.news.entity.NewsStatus
import com.semihsahinoglu.sportseus.news.exception.NewsNotFoundException
import com.semihsahinoglu.sportseus.news.exception.NewsSlugConflictException
import com.semihsahinoglu.sportseus.news.mapper.NewsMapper
import com.semihsahinoglu.sportseus.news.repository.NewsRepository
import com.semihsahinoglu.sportseus.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class NewsService(
    private val newsRepository: NewsRepository,
    private val tagService: TagService,
    private val newsMapper: NewsMapper,
) {
    // ADMIN: haber oluştur (author JWT'den, slug çakışırsa 409)
    @Transactional
    fun create(request: NewsCreateRequest, author: User): NewsResponse {
        if (newsRepository.existsBySlug(request.slug)) throw NewsSlugConflictException("Bu slug zaten kullanılıyor: ${request.slug}")

        val news = newsMapper.toEntity(request, author)

        // status kuralı (PUBLISHED ise publishDate set)
        news.changeStatus(request.status)

        // relations (snapshot)
        request.relations.forEach { input ->
            news.relations.add(newsMapper.toRelationEntity(news, input))
        }

        // tags (get-or-create)
        news.tags.addAll(tagService.resolveTags(request.tags))

        val saved = newsRepository.save(news)
        return newsMapper.toResponse(saved)
    }

    // ADMIN: güncelle (partial, relations/tags replace)
    @Transactional
    fun update(id: UUID, request: NewsUpdateRequest): NewsResponse {
        val news = newsRepository.findWithRelationsById(id)
            ?: throw NewsNotFoundException("Haber bulunamadı: id=$id")

        // slug değişimi — çakışma kontrolü (kendi slug'ı hariç)
        request.slug?.let { newSlug ->
            if (newSlug != news.slug && newsRepository.existsBySlug(newSlug))
                throw NewsSlugConflictException("Bu slug zaten kullanılıyor: $newSlug")
            news.slug = newSlug
        }

        news.applyUpdate(
            request.title,
            request.content,
            request.imageUrl,
            request.category,
            request.breaking,
            request.status,
            request.featured
        )
        // relations replace (verilmişse)
        request.relations?.let { inputs ->
            news.relations.clear()
            inputs.forEach { news.relations.add(newsMapper.toRelationEntity(news, it)) }
        }

        // tags replace (verilmişse)
        request.tags?.let { tagNames ->
            news.tags.clear()
            news.tags.addAll(tagService.resolveTags(tagNames))
        }

        return newsMapper.toResponse(newsRepository.save(news))
    }

    // PUBLIC: slug ile tek haber (sadece yayında)
    @Transactional(readOnly = true)
    fun getBySlug(slug: String): NewsResponse {
        val news = newsRepository.findBySlug(slug)
            ?: throw NewsNotFoundException("Haber bulunamadı: slug=$slug")
        if (news.status != NewsStatus.PUBLISHED) throw NewsNotFoundException("Haber bulunamadı: slug=$slug")
        return newsMapper.toResponse(news)
    }

    // PUBLIC: yayındaki haberler (sayfalı)
    @Transactional(readOnly = true)
    fun getPublished(pageable: Pageable): Page<NewsListItemResponse> =
        newsRepository.findAllByStatusOrderByPublishDateDesc(NewsStatus.PUBLISHED, pageable)
            .map(newsMapper::toListItem)

    // PUBLIC: kategoriye göre
    @Transactional(readOnly = true)
    fun getByCategory(category: NewsCategory, pageable: Pageable): Page<NewsListItemResponse> =
        newsRepository.findAllByStatusAndCategoryOrderByPublishDateDesc(NewsStatus.PUBLISHED, category, pageable)
            .map(newsMapper::toListItem)

    // PUBLIC: son dakika
    @Transactional(readOnly = true)
    fun getBreaking(pageable: Pageable): Page<NewsListItemResponse> =
        newsRepository.findAllByStatusAndBreakingTrueOrderByPublishDateDesc(NewsStatus.PUBLISHED, pageable)
            .map(newsMapper::toListItem)

    // PUBLIC: featured
    @Transactional(readOnly = true)
    fun getFeatured(pageable: Pageable): Page<NewsListItemResponse> =
        newsRepository.findAllByStatusAndFeaturedTrueOrderByPublishDateDesc(NewsStatus.PUBLISHED, pageable)
            .map(newsMapper::toListItem)

    // PUBLIC: bir entity'ye bağlı haberler (snapshot)
    @Transactional(readOnly = true)
    fun getByRelation(type: NewsRelationType, externalId: Int, pageable: Pageable): Page<NewsListItemResponse> =
        newsRepository.findByRelation(type, externalId, NewsStatus.PUBLISHED, pageable)
            .map(newsMapper::toListItem)

    // ADMIN: tüm haberler (taslak dahil)
    @Transactional(readOnly = true)
    fun getAllForAdmin(pageable: Pageable): Page<NewsListItemResponse> =
        newsRepository.findAllByOrderByCreatedDateDesc(pageable)
            .map(newsMapper::toListItem)

    // ADMIN: id ile detay (taslak dahil — editor önizleme)
    @Transactional(readOnly = true)
    fun getByIdForAdmin(id: UUID): NewsResponse {
        val news = newsRepository.findWithRelationsById(id)
            ?: throw NewsNotFoundException("Haber bulunamadı: id=$id")
        return newsMapper.toResponse(news)
    }

    // ADMIN: sil (relations cascade, tags M2M köprü otomatik temizlenir)
    @Transactional
    fun delete(id: UUID) {
        if (!newsRepository.existsById(id)) throw NewsNotFoundException("Haber bulunamadı: id=$id")
        newsRepository.deleteById(id)
    }
}