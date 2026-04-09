package com.controlzero.api.rol

import org.springframework.data.jpa.repository.JpaRepository

interface RolRepository : JpaRepository<Rol, Long> {
    fun existsByNombreIgnoreCase(nombre: String): Boolean
}
