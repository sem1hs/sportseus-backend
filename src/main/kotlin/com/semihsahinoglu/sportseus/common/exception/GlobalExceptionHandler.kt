package com.semihsahinoglu.sportseus.common.exception

import com.semihsahinoglu.sportseus.auth.exception.AuthenticationFailedException
import com.semihsahinoglu.sportseus.auth.exception.InvalidRefreshTokenException
import com.semihsahinoglu.sportseus.auth.exception.RefreshTokenDoesntBelongUserException
import com.semihsahinoglu.sportseus.auth.exception.RefreshTokenNotFoundException
import com.semihsahinoglu.sportseus.coach.exception.CoachCareerConflictException
import com.semihsahinoglu.sportseus.coach.exception.CoachNotFoundException
import com.semihsahinoglu.sportseus.common.dto.ApiResponse
import com.semihsahinoglu.sportseus.fixture.exception.FixtureMissingReferencesException
import com.semihsahinoglu.sportseus.fixture.exception.FixtureNotFoundException
import com.semihsahinoglu.sportseus.league.exception.LeagueAlreadyExistsException
import com.semihsahinoglu.sportseus.league.exception.LeagueNotFoundException
import com.semihsahinoglu.sportseus.league.exception.LeagueNotFoundInApiException
import com.semihsahinoglu.sportseus.lineup.exception.LineupConflictException
import com.semihsahinoglu.sportseus.lineup.exception.LineupNotFoundException
import com.semihsahinoglu.sportseus.lineup.exception.LineupPlayerConflictException
import com.semihsahinoglu.sportseus.news.exception.NewsNotFoundException
import com.semihsahinoglu.sportseus.news.exception.NewsSlugConflictException
import com.semihsahinoglu.sportseus.player.exception.MissingReferencesException
import com.semihsahinoglu.sportseus.player.exception.PlayerNotFoundException
import com.semihsahinoglu.sportseus.player.exception.PlayerStatisticsConflictException
import com.semihsahinoglu.sportseus.player.exception.PlayerStatisticsNotFoundException
import com.semihsahinoglu.sportseus.player.exception.PlayerTeamConflictException
import com.semihsahinoglu.sportseus.player.exception.PlayerTeamNotFoundException
import com.semihsahinoglu.sportseus.standing.exception.StandingConflictException
import com.semihsahinoglu.sportseus.standing.exception.StandingNotFoundException
import com.semihsahinoglu.sportseus.team.exception.LeagueTeamConflictException
import com.semihsahinoglu.sportseus.team.exception.LeagueTeamNotFoundException
import com.semihsahinoglu.sportseus.team.exception.TeamNotFoundException
import com.semihsahinoglu.sportseus.team.exception.TeamStatisticsConflictException
import com.semihsahinoglu.sportseus.team.exception.TeamStatisticsNotFoundException
import com.semihsahinoglu.sportseus.transfer.exception.TransferConflictException
import com.semihsahinoglu.sportseus.transfer.exception.TransferNotFoundException
import com.semihsahinoglu.sportseus.user.exception.UserAlreadyExistException
import com.semihsahinoglu.sportseus.user.exception.UserNotFoundException
import com.semihsahinoglu.sportseus.venue.exception.VenueNotFoundException
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

    @ExceptionHandler(LeagueAlreadyExistsException::class)
    fun handleLeagueAlreadyExistsException(
        ex: LeagueAlreadyExistsException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            "League Already Exist",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(LeagueNotFoundException::class)
    fun handleLeagueNotFoundException(
        ex: LeagueNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "League Not Found",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(LeagueNotFoundInApiException::class)
    fun handleLeagueNotFoundInApiException(
        ex: LeagueNotFoundInApiException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "League Not Found in API",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(TeamNotFoundException::class)
    fun handleTeamNotFoundException(
        ex: TeamNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "Team Not Found",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(TeamStatisticsNotFoundException::class)
    fun handleTeamStatisticsNotFoundException(
        ex: TeamStatisticsNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "Team Statistics Not Found",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(PlayerNotFoundException::class)
    fun handlePlayerNotFoundException(
        ex: PlayerNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "Player Not Found",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(PlayerStatisticsNotFoundException::class)
    fun handlePlayerStatisticsNotFoundException(
        ex: PlayerStatisticsNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "Player Statistics Not Found",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(MissingReferencesException::class)
    fun MissingReferencesException(
        ex: MissingReferencesException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Missing References",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(PlayerTeamNotFoundException::class)
    fun handlePlayerTeamNotFoundException(
        ex: PlayerTeamNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "Player Team Not Found",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(TransferNotFoundException::class)
    fun handleTransferNotFoundException(
        ex: TransferNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "Transfer Not Found",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(TransferConflictException::class)
    fun handleTransferConflictException(
        ex: TransferConflictException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.CONFLICT,
            "Transfer Conflict",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(VenueNotFoundException::class)
    fun handleVenueNotFoundException(
        ex: VenueNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "Venue Not Found",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(FixtureNotFoundException::class)
    fun handleFixtureNotFoundException(
        ex: FixtureNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "Fixture Not Found",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(FixtureMissingReferencesException::class)
    fun handleFixtureMissingReferencesException(
        ex: FixtureMissingReferencesException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Missing References",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(CoachNotFoundException::class)
    fun CoachNotFoundException(
        ex: CoachNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "Coach Not Found",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(LineupNotFoundException::class)
    fun LineupNotFoundException(
        ex: LineupNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "Lineup Not Found",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(LineupPlayerConflictException::class)
    fun LineupPlayerConflictException(
        ex: LineupPlayerConflictException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.CONFLICT,
            "Lineup Player Conflict",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(StandingNotFoundException::class)
    fun StandingNotFoundException(
        ex: StandingNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "Standing Not Found",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(CoachCareerConflictException::class)
    fun CoachCareerConflictException(
        ex: CoachCareerConflictException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.CONFLICT,
            "Coach Career Conflict",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(LineupConflictException::class)
    fun LineupConflictException(
        ex: LineupConflictException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.CONFLICT,
            "Lineup Conflict",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(PlayerStatisticsConflictException::class)
    fun PlayerStatisticsConflictException(
        ex: PlayerStatisticsConflictException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.CONFLICT,
            "Player Statistics Conflict",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(PlayerTeamConflictException::class)
    fun PlayerTeamConflictException(
        ex: PlayerTeamConflictException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.CONFLICT,
            "Player Team Conflict",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(StandingConflictException::class)
    fun StandingConflictException(
        ex: StandingConflictException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.CONFLICT,
            "Standing Conflict",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(LeagueTeamConflictException::class)
    fun LeagueTeamConflictException(
        ex: LeagueTeamConflictException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.CONFLICT,
            "League Team Conflict",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(LeagueTeamNotFoundException::class)
    fun LeagueTeamNotFoundException(
        ex: LeagueTeamNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "League Team Not Found",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(TeamStatisticsConflictException::class)
    fun TeamStatisticsConflictException(
        ex: TeamStatisticsConflictException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.CONFLICT,
            "Team Statistics Conflict",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(NewsSlugConflictException::class)
    fun NewsSlugConflictException(
        ex: NewsSlugConflictException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.CONFLICT,
            "News Slug Conflict",
            ex.message,
            request.requestURI
        )
    }

    @ExceptionHandler(NewsNotFoundException::class)
    fun NewsNotFoundException(
        ex: NewsNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<ErrorResponse>> {
        return ErrorResponse.buildErrorResponse(
            HttpStatus.NOT_FOUND,
            "News Not Found",
            ex.message,
            request.requestURI
        )
    }
}