package com.semihsahinoglu.sportseus.league.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class LeagueNotFoundInApiException(message: String = "League not found in API"): RuntimeException(message)