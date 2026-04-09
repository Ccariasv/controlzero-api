package com.controlzero.api.permiso

import org.springframework.data.jpa.repository.JpaRepository

interface PermisoRepository : JpaRepository<Permiso, Long> {
    fun existsByNombreIgnoreCase(nombre: String): Boolean
}
