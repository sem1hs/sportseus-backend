package com.semihsahinoglu.sportseus.football.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
class FootballApiRateLimitException(messages: String = "Api Football Rate Limit Error") : RuntimeException(messages)