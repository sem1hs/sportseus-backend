package com.semihsahinoglu.sportseus.common.exception

import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

data class ErrorResponse(
    val status: Int,
    val error: String,
    val message: String?,
    val path: String,
    val timestamp: LocalDateTime
) {
    companion object {

        fun buildErrorResponse(
            status: HttpStatus,
            error: String,
            message: String?,
            path: String
        ): ResponseEntity<ApiResponse<ErrorResponse>> {
            val errorResponse = ErrorResponse(status.value(), error, message, path, LocalDateTime.now())
            val apiResponse = ApiResponse.fail(errorResponse)

            return ResponseEntity.status(errorResponse.status).body(apiResponse)
        }
    }
}
