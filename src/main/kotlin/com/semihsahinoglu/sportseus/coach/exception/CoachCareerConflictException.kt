package com.semihsahinoglu.sportseus.coach.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class CoachCareerConflictException(message: String = "Coach career conflict") : RuntimeException(message)