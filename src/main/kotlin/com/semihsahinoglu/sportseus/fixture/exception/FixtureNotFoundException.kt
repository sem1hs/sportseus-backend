package com.semihsahinoglu.sportseus.fixture.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND)
class FixtureNotFoundException(message: String = "Fixture not found") : RuntimeException(message)
