package com.controlzero.api.common.exception

import com.controlzero.api.common.dto.ApiError
import jakarta.servlet.http.HttpServletRequest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val message = ex.bindingResult
            .allErrors
            .joinToString("; ") { error ->
                if (error is FieldError) "${error.field}: ${error.defaultMessage}" else error.defaultMessage ?: "validation error"
            }

        return ResponseEntity.badRequest().body(
            ApiError(
                status = HttpStatus.BAD_REQUEST.value(),
                error = HttpStatus.BAD_REQUEST.reasonPhrase,
                message = message,
                path = request.requestURI
            )
        )
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequest(ex: BadRequestException, request: HttpServletRequest): ResponseEntity<ApiError> {
        return ResponseEntity.badRequest().body(
            ApiError(
                status = HttpStatus.BAD_REQUEST.value(),
                error = HttpStatus.BAD_REQUEST.reasonPhrase,
                message = ex.message ?: "bad request",
                path = request.requestURI
            )
        )
    }

    @ExceptionHandler(ConflictException::class)
    fun handleConflict(ex: ConflictException, request: HttpServletRequest): ResponseEntity<ApiError> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ApiError(
                status = HttpStatus.CONFLICT.value(),
                error = HttpStatus.CONFLICT.reasonPhrase,
                message = ex.message ?: "conflict",
                path = request.requestURI
            )
        )
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(ex: NotFoundException, request: HttpServletRequest): ResponseEntity<ApiError> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ApiError(
                status = HttpStatus.NOT_FOUND.value(),
                error = HttpStatus.NOT_FOUND.reasonPhrase,
                message = ex.message ?: "resource not found",
                path = request.requestURI
            )
        )
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorized(ex: UnauthorizedException, request: HttpServletRequest): ResponseEntity<ApiError> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ApiError(
                status = HttpStatus.UNAUTHORIZED.value(),
                error = HttpStatus.UNAUTHORIZED.reasonPhrase,
                message = ex.message ?: "unauthorized",
                path = request.requestURI
            )
        )
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrity(ex: DataIntegrityViolationException, request: HttpServletRequest): ResponseEntity<ApiError> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ApiError(
                status = HttpStatus.CONFLICT.value(),
                error = HttpStatus.CONFLICT.reasonPhrase,
                message = "No se pudo completar la operación por una restricción de datos",
                path = request.requestURI
            )
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleAny(ex: Exception, request: HttpServletRequest): ResponseEntity<ApiError> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ApiError(
                status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
                message = ex.message ?: "internal server error",
                path = request.requestURI
            )
        )
    }
}
