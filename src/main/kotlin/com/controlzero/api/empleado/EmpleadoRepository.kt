package com.controlzero.api.empleado

import org.springframework.data.jpa.repository.JpaRepository

interface EmpleadoRepository : JpaRepository<Empleado, Long> {
    fun existsByDpi(dpi: String): Boolean
}
