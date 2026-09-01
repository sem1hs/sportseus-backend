package com.semihsahinoglu.sportseus.team.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class LeagueTeamNotFoundException(message: String = "League team not found") : RuntimeException(message)