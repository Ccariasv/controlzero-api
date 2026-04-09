package com.controlzero.api.usuario.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.Instant

data class UsuarioRequest(
    @field:NotNull
    val empleadoId: Long,

    @field:NotNull
    val rolId: Long,

    @field:Email
    @field:NotBlank
    @field:Size(max = 150)
    val email: String,

    @field:NotBlank
    @field:Size(min = 8, max = 120)
    val password: String,

    val activo: Boolean = true
)

data class UsuarioUpdateRequest(
    val rolId: Long? = null,
    @field:Email
    @field:Size(max = 150)
    val email: String? = null,
    @field:Size(min = 8, max = 120)
    val password: String? = null,
    val activo: Boolean? = null
)

data class UsuarioResponse(
    val id: Long,
    val empleadoId: Long,
    val empleadoNombre: String,
    val rolId: Long,
    val rolNombre: String,
    val email: String,
    val activo: Boolean,
    val ultimoLogin: Instant?
)
