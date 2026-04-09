package com.controlzero.api.permiso

import com.controlzero.api.common.exception.ConflictException
import com.controlzero.api.common.exception.NotFoundException
import com.controlzero.api.permiso.dto.PermisoRequest
import com.controlzero.api.permiso.dto.PermisoResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PermisoService(
    private val permisoRepository: PermisoRepository
) {

    @Transactional(readOnly = true)
    fun findAll(): List<PermisoResponse> = permisoRepository.findAll().map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): PermisoResponse = getPermiso(id).toResponse()

    @Transactional
    fun create(request: PermisoRequest): PermisoResponse {
        if (permisoRepository.existsByNombreIgnoreCase(request.nombre.trim())) {
            throw ConflictException("Ya existe un permiso con el nombre '${request.nombre}'")
        }

        val permiso = Permiso(
            nombre = request.nombre.trim(),
            descripcion = request.descripcion?.trim()
        )
        return permisoRepository.save(permiso).toResponse()
    }

    @Transactional
    fun update(id: Long, request: PermisoRequest): PermisoResponse {
        val permiso = getPermiso(id)
        val newName = request.nombre.trim()

        if (!permiso.nombre.equals(newName, ignoreCase = true) && permisoRepository.existsByNombreIgnoreCase(newName)) {
            throw ConflictException("Ya existe un permiso con el nombre '$newName'")
        }

        permiso.nombre = newName
        permiso.descripcion = request.descripcion?.trim()
        return permisoRepository.save(permiso).toResponse()
    }

    @Transactional
    fun delete(id: Long) {
        val permiso = getPermiso(id)
        permisoRepository.delete(permiso)
    }

    @Transactional(readOnly = true)
    fun findEntitiesByIds(ids: Set<Long>): MutableSet<Permiso> {
        if (ids.isEmpty()) return mutableSetOf()
        val permisos = permisoRepository.findAllById(ids)
        if (permisos.size != ids.size) {
            throw NotFoundException("Uno o más permisos no existen")
        }
        return permisos.toMutableSet()
    }

    private fun getPermiso(id: Long): Permiso {
        return permisoRepository.findById(id)
            .orElseThrow { NotFoundException("Permiso con id $id no encontrado") }
    }

    private fun Permiso.toResponse() = PermisoResponse(
        id = id ?: 0L,
        nombre = nombre,
        descripcion = descripcion
    )
}
