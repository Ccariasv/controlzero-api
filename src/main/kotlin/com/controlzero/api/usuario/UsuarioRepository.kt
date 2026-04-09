package com.controlzero.api.usuario

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UsuarioRepository : JpaRepository<Usuario, Long> {
    fun findByEmail(email: String): Optional<Usuario>
    fun existsByEmail(email: String): Boolean
    fun existsByEmpleadoId(empleadoId: Long): Boolean
}
