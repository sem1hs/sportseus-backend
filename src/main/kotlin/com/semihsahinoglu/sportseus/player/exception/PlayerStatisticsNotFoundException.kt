package com.semihsahinoglu.sportseus.player.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class PlayerStatisticsNotFoundException(message: String = "Player statistic not found") : RuntimeException(message)
