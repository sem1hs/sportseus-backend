package com.semihsahinoglu.sportseus.news.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.CONFLICT)
class NewsSlugConflictException(message: String) : RuntimeException(message)