package com.semihsahinoglu.sportseus.team.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class TeamStatisticsConflictException(message: String = "Team statistics conflict") : RuntimeException(message)