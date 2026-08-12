package com.semihsahinoglu.sportseus.auth.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class RefreshTokenDoesntBelongUserException(message : String = "Refresh Token kullanıcıya ait değil") : RuntimeException(message)