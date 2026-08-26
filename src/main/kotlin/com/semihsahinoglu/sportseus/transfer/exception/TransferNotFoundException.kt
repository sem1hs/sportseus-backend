package com.semihsahinoglu.sportseus.transfer.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class TransferNotFoundException(message: String = "Transfer not found") : RuntimeException(message)
