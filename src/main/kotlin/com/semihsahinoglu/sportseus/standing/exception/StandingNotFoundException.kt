package com.semihsahinoglu.sportseus.standing.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class StandingNotFoundException(message: String = "Standing not found") : RuntimeException(message)