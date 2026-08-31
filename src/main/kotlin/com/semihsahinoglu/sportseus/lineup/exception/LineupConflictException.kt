package com.semihsahinoglu.sportseus.lineup.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class LineupConflictException(message: String = "Lineup conflict") : RuntimeException(message)