package com.controlzero.api.rol

import com.controlzero.api.common.exception.ConflictException
import com.controlzero.api.common.exception.NotFoundException
import com.controlzero.api.permiso.PermisoService
import com.controlzero.api.permiso.dto.PermisoResponse
import com.controlzero.api.rol.dto.RolRequest
import com.controlzero.api.rol.dto.RolResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class RolService(
    private val rolRepository: RolRepository,
    private val permisoService: PermisoService
) {

    @Transactional(readOnly = true)
    fun findAll(): List<RolResponse> = rolRepository.findAll().map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): RolResponse = getRol(id).toResponse()

    @Transactional
    fun create(request: RolRequest): RolResponse {
        val name = request.nombre.trim()
        if (rolRepository.existsByNombreIgnoreCase(name)) {
            throw ConflictException("Ya existe un rol con nombre '$name'")
        }

        val rol = Rol(
            nombre = name,
            descripcion = request.descripcion?.trim(),
            permisos = permisoService.findEntitiesByIds(request.permisoIds)
        )

        return rolRepository.save(rol).toResponse()
    }

    @Transactional
    fun update(id: Long, request: RolRequest): RolResponse {
        val rol = getRol(id)
        val newName = request.nombre.trim()
        if (!rol.nombre.equals(newName, ignoreCase = true) && rolRepository.existsByNombreIgnoreCase(newName)) {
            throw ConflictException("Ya existe un rol con nombre '$newName'")
        }

        rol.nombre = newName
        rol.descripcion = request.descripcion?.trim()
        rol.permisos = permisoService.findEntitiesByIds(request.permisoIds)
        rol.actualizadoEn = Instant.now()

        return rolRepository.save(rol).toResponse()
    }

    @Transactional
    fun delete(id: Long) {
        val rol = getRol(id)
        rolRepository.delete(rol)
    }

    @Transactional(readOnly = true)
    fun getRol(id: Long): Rol {
        return rolRepository.findById(id)
            .orElseThrow { NotFoundException("Rol con id $id no encontrado") }
    }

    private fun Rol.toResponse() = RolResponse(
        id = id ?: 0L,
        nombre = nombre,
        descripcion = descripcion,
        permisos = permisos
            .map { PermisoResponse(it.id ?: 0L, it.nombre, it.descripcion) }
            .sortedBy { it.nombre.lowercase() }
    )
}
