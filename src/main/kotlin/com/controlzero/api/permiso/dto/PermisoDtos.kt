package com.controlzero.api.permiso.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class PermisoRequest(
    @field:NotBlank
    @field:Size(max = 100)
    val nombre: String,

    @field:Size(max = 255)
    val descripcion: String? = null
)

data class PermisoResponse(
    val id: Long,
    val nombre: String,
    val descripcion: String?
)
