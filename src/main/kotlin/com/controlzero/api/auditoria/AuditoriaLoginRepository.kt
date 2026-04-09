package com.controlzero.api.auditoria

import org.springframework.data.jpa.repository.JpaRepository

interface AuditoriaLoginRepository : JpaRepository<AuditoriaLogin, Long> {
    fun findAllByOrderByFechaEventoDesc(): List<AuditoriaLogin>
}
