package com.semihsahinoglu.sportseus.league.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
class LeagueAlreadyExistsException(message: String = "League already exists") : RuntimeException(message)