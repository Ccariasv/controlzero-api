package com.controlzero.api.empleado

import org.springframework.data.jpa.repository.JpaRepository

interface EmpleadoPuestoRepository : JpaRepository<EmpleadoPuesto, Long> {
    fun existsByNombreIgnoreCase(nombre: String): Boolean
}
