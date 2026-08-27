package com.semihsahinoglu.sportseus.venue.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class VenueNotFoundException(message: String = "Venue not found") : RuntimeException(message)