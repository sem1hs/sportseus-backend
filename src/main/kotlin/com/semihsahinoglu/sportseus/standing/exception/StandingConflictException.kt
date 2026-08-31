package com.semihsahinoglu.sportseus.standing.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.CONFLICT)
class StandingConflictException(message: String) : RuntimeException(message)