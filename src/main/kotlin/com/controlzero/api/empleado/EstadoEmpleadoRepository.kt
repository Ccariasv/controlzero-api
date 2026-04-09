package com.controlzero.api.empleado

import org.springframework.data.jpa.repository.JpaRepository

interface EstadoEmpleadoRepository : JpaRepository<EstadoEmpleado, Long> {
    fun existsByNombreIgnoreCase(nombre: String): Boolean
}
