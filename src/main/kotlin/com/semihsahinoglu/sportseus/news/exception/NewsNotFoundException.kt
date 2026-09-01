package com.semihsahinoglu.sportseus.news.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class NewsNotFoundException(message: String) : RuntimeException(message)
