package com.semihsahinoglu.sportseus.auth.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class RefreshTokenNotFoundException(message: String = "Refresh Token bulunamadı") : RuntimeException(message)