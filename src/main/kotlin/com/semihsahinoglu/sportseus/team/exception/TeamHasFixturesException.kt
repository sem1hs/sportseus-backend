package com.semihsahinoglu.sportseus.team.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class TeamHasFixturesException(message: String = "Team has fixtures") : RuntimeException(message)