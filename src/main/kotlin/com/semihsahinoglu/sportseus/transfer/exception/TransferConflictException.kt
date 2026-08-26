package com.semihsahinoglu.sportseus.transfer.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.CONFLICT)
class TransferConflictException(message: String = "Transfer conflict") : RuntimeException(message)