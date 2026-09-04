package com.semihsahinoglu.sportseus.news.controller

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.news.dto.TagResponse
import com.semihsahinoglu.sportseus.news.service.TagService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tags")
class TagController(
    private val tagService: TagService
) {

    @GetMapping("/latest")
    fun latestTags(): ResponseEntity<ApiResponse<List<TagResponse>>> {
        val tags = tagService.getLatestTags()
        val response = ApiResponse.success(tags)
        return ResponseEntity.ok(response)
    }
}