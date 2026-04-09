package com.controlzero.api.auditoria.dto

import java.time.Instant

data class AuditoriaLoginResponse(
    val id: Long,
    val usuarioId: Long?,
    val emailIntento: String?,
    val fechaEvento: Instant,
    val exito: Boolean,
    val ipAddress: String?,
    val userAgent: String?,
    val detalle: String?
)
