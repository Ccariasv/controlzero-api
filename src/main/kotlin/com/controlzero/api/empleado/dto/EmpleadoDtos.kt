package com.controlzero.api.empleado.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class EstadoEmpleadoRequest(
    @field:NotBlank
    @field:Size(max = 50)
    val nombre: String,

    @field:Size(max = 150)
    val descripcion: String? = null
)

data class EstadoEmpleadoResponse(
    val id: Long,
    val nombre: String,
    val descripcion: String?
)

data class EmpleadoPuestoRequest(
    @field:NotBlank
    @field:Size(max = 150)
    val nombre: String,

    @field:Size(max = 255)
    val descripcion: String? = null
)

data class EmpleadoPuestoResponse(
    val id: Long,
    val nombre: String,
    val descripcion: String?
)

data class EmpleadoRequest(
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
    val fotoUrl: String? = null
)

data class EmpleadoResponse(
    val id: Long,
    val nombres: String,
    val apellidos: String,
    val dpi: String,
    val telefono: String?,
    val estado: EstadoEmpleadoResponse,
    val puesto: EmpleadoPuestoResponse?,
    val fechaIngreso: LocalDate,
    val fotoUrl: String?
)
