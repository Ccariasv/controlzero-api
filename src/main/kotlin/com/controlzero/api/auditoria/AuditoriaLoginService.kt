package com.controlzero.api.auditoria

import com.controlzero.api.auditoria.dto.AuditoriaLoginResponse
import com.controlzero.api.common.exception.NotFoundException
import com.controlzero.api.usuario.Usuario
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuditoriaLoginService(
    private val auditoriaLoginRepository: AuditoriaLoginRepository
) {

    @Transactional(readOnly = true)
    fun findAll(): List<AuditoriaLoginResponse> {
        return auditoriaLoginRepository.findAllByOrderByFechaEventoDesc().map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): AuditoriaLoginResponse {
        val audit = auditoriaLoginRepository.findById(id)
            .orElseThrow { NotFoundException("Registro de auditoría con id $id no encontrado") }
        return audit.toResponse()
    }

    @Transactional
    fun saveLoginAttempt(
        usuario: Usuario?,
        emailIntento: String?,
        exito: Boolean,
        ipAddress: String?,
        userAgent: String?,
        detalle: String?
    ) {
        auditoriaLoginRepository.save(
            AuditoriaLogin(
                usuario = usuario,
                emailIntento = emailIntento?.trim()?.lowercase(),
                exito = exito,
                ipAddress = ipAddress,
                userAgent = userAgent,
                detalle = detalle
            )
        )
    }

    private fun AuditoriaLogin.toResponse() = AuditoriaLoginResponse(
        id = id ?: 0L,
        usuarioId = usuario?.id,
        emailIntento = emailIntento,
        fechaEvento = fechaEvento,
        exito = exito,
        ipAddress = ipAddress,
        userAgent = userAgent,
        detalle = detalle
    )
}
