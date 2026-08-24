package com.semihsahinoglu.sportseus.team.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class TeamStatisticsNotFoundException(message: String) : RuntimeException(message)