package com.controlzero.api.rol.dto

import com.controlzero.api.permiso.dto.PermisoResponse
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RolRequest(
    @field:NotBlank
    @field:Size(max = 50)
    val nombre: String,

    @field:Size(max = 150)
    val descripcion: String? = null,

    val permisoIds: Set<Long> = emptySet()
)

data class RolResponse(
    val id: Long,
    val nombre: String,
    val descripcion: String?,
    val permisos: List<PermisoResponse>
)
