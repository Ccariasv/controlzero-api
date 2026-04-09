package com.controlzero.api.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class RegisterRequest(
    @field:NotBlank
    @field:Size(max = 100)
    val nombres: String,

    @field:NotBlank
    @field:Size(max = 100)
    val apellidos: String,

    @field:NotBlank
    @field:Size(max = 30)
    val dpi: String,

    @field:Size(max = 20)
    val telefono: String? = null,

    val empleadoPuestoId: Long? = null,

    @field:NotNull
    val estadoEmpleadoId: Long,

    @field:NotNull
    val fechaIngreso: LocalDate,

    @field:Size(max = 255)
    val fotoUrl: String? = null,

    @field:NotNull
    val rolId: Long,

    @field:Email
    @field:NotBlank
    @field:Size(max = 150)
    val email: String,

    @field:NotBlank
    @field:Size(min = 8, max = 120)
    val password: String
)

data class LoginRequest(
    @field:Email
    @field:NotBlank
    val email: String,

    @field:NotBlank
    val password: String
)

data class RefreshTokenRequest(
    @field:NotBlank
    val refreshToken: String
)

data class AuthTokensResponse(
    val sessionToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
    val expiresInSeconds: Long
)

data class AuthUserResponse(
    val usuarioId: Long,
    val empleadoId: Long,
    val email: String,
    val rol: String
)

data class RegisterResponse(
    val usuario: AuthUserResponse
)
