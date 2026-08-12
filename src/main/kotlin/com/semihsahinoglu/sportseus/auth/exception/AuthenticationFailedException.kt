package com.semihsahinoglu.sportseus.auth.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class AuthenticationFailedException(message: String = "Kimlik doğrulama başarısız") : RuntimeException(message)