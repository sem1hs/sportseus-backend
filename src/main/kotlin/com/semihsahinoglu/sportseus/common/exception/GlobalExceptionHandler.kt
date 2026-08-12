package com.semihsahinoglu.sportseus.common.exception

import com.semihsahinoglu.sportseus.auth.exception.AuthenticationFailedException
import com.semihsahinoglu.sportseus.auth.exception.InvalidRefreshTokenException
import com.semihsahinoglu.sportseus.auth.exception.RefreshTokenDoesntBelongUserException
import com.semihsahinoglu.sportseus.auth.exception.RefreshTokenNotFoundException
import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.user.exception.UserAlreadyExistException
import com.semihsahinoglu.sportseus.user.exception.UserNotFoundException
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {

        val message = ex.bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
        val errorResponse = ErrorResponse(
            status.value(),
            "Validation Error",
            message,
            request.getDescription(false).replace("uri=", ""),
            LocalDateTime.now()
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(
        ex: UserNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "User Not Found",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(UserAlreadyExistException::class)
    fun handleUserAlreadyExistException(
        ex: UserAlreadyExistException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.CONFLICT,
            "User Already Exist",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(AuthenticationFailedException::class)
    fun handleAuthenticationFailedException(
        ex: AuthenticationFailedException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.FORBIDDEN,
            "Authentication Failed",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(InvalidRefreshTokenException::class)
    fun handleInvalidRefreshTokenException(
        ex: InvalidRefreshTokenException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Invalid Refresh Token",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(RefreshTokenDoesntBelongUserException::class)
    fun handleRefreshTokenDoesntBelongUserException(
        ex: RefreshTokenDoesntBelongUserException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Refresh Token Doesn't Belong User",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(RefreshTokenNotFoundException::class)
    fun handleRefreshTokenNotFoundException(
        ex: RefreshTokenNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "Refresh Token Not Found",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(ExpiredJwtException::class)
    fun handleExpiredJwt(
        ex: ExpiredJwtException,
        request: HttpServletRequest,
    ): ResponseEntity<ApiResponse<ErrorResponse>> =
        ErrorResponse.buildErrorResponse(
            HttpStatus.UNAUTHORIZED,
            "Token Expired",
            ex.message,
            request.requestURI,
        )

    @ExceptionHandler(JwtException::class)
    fun handleInvalidJwt(
        ex: JwtException,
        request: HttpServletRequest,
    ): ResponseEntity<ApiResponse<ErrorResponse>> =
        ErrorResponse.buildErrorResponse(
            HttpStatus.UNAUTHORIZED,
            "Invalid Token",
            ex.message,
            request.requestURI,
        )
}