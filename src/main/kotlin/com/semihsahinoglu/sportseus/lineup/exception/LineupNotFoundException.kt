package com.semihsahinoglu.sportseus.lineup.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class LineupNotFoundException(message: String = "Lineup not found") : RuntimeException(message)