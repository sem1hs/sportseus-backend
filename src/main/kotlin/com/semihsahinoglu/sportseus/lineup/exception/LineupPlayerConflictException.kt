package com.semihsahinoglu.sportseus.lineup.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.CONFLICT)
class LineupPlayerConflictException(message: String = "Lineup player conflict") : RuntimeException(message)