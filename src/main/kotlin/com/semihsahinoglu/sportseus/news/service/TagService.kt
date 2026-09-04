package com.semihsahinoglu.sportseus.news.service

import com.semihsahinoglu.sportseus.news.dto.TagResponse
import com.semihsahinoglu.sportseus.news.entity.Tag
import com.semihsahinoglu.sportseus.news.repository.TagRepository
import org.springframework.stereotype.Service
import java.text.Normalizer

@Service
class TagService(
    private val tagRepository: TagRepository
) {

    // METHOD: tag get-or-create (isim listesi → Tag entity seti)
    fun resolveTags(names: List<String>): Set<Tag> =
        names.map { name ->
            val slug = slugify(name)
            tagRepository.findBySlug(slug)
                ?: tagRepository.save(Tag(name = name, slug = slug))
        }.toSet()

    // METHOD: basit slugify (tag slug üretimi)
    private fun slugify(text: String): String =
        Normalizer.normalize(text, Normalizer.Form.NFD)
            .replace(Regex("[^\\p{ASCII}]"), "")
            .lowercase()
            .replace(Regex("[^a-z0-9\\s-]"), "")
            .trim()
            .replace(Regex("\\s+"), "-")

    fun getLatestTags(): List<TagResponse> =
        tagRepository.findTop5ByOrderByCreatedDateDesc().map { TagResponse(it.id!!, it.name, it.slug) }
}