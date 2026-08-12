package com.semihsahinoglu.sportseus.auth.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class InvalidRefreshTokenException(message: String = "Refresh Token geçersiz") : RuntimeException(message)