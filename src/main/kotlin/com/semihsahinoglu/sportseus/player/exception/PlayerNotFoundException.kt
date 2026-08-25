package com.semihsahinoglu.sportseus.player.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class PlayerNotFoundException(message: String = "Player not found") : RuntimeException(message)