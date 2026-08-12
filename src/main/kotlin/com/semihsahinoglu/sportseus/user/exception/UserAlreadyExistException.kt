package com.semihsahinoglu.sportseus.user.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.CONFLICT)
class UserAlreadyExistException(message: String = "Kullanıcı zaten kayıtlı") : RuntimeException(message)