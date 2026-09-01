package com.semihsahinoglu.sportseus.team.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.CONFLICT)
class LeagueTeamConflictException(message: String = "League team conflict") : RuntimeException(message)