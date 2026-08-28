package com.semihsahinoglu.sportseus.coach.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class CoachNotFoundException(message: String = "Coach not found") : RuntimeException(message)