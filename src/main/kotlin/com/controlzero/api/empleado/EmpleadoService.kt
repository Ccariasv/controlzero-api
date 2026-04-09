package com.controlzero.api.empleado

import com.controlzero.api.common.exception.ConflictException
import com.controlzero.api.common.exception.NotFoundException
import com.controlzero.api.empleado.dto.EmpleadoPuestoRequest
import com.controlzero.api.empleado.dto.EmpleadoPuestoResponse
import com.controlzero.api.empleado.dto.EmpleadoRequest
import com.controlzero.api.empleado.dto.EmpleadoResponse
import com.controlzero.api.empleado.dto.EstadoEmpleadoRequest
import com.controlzero.api.empleado.dto.EstadoEmpleadoResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class EmpleadoService(
    private val empleadoRepository: EmpleadoRepository,
    private val estadoEmpleadoRepository: EstadoEmpleadoRepository,
    private val empleadoPuestoRepository: EmpleadoPuestoRepository
) {

    @Transactional(readOnly = true)
    fun findAllEstados(): List<EstadoEmpleadoResponse> {
        return estadoEmpleadoRepository.findAll().map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun findEstadoById(id: Long): EstadoEmpleadoResponse = getEstado(id).toResponse()

    @Transactional
    fun createEstado(request: EstadoEmpleadoRequest): EstadoEmpleadoResponse {
        val name = request.nombre.trim()
        if (estadoEmpleadoRepository.existsByNombreIgnoreCase(name)) {
            throw ConflictException("Ya existe un estado de empleado llamado '$name'")
        }
        val estado = EstadoEmpleado(
            nombre = name,
            descripcion = request.descripcion?.trim()
        )
        return estadoEmpleadoRepository.save(estado).toResponse()
    }

    @Transactional
    fun updateEstado(id: Long, request: EstadoEmpleadoRequest): EstadoEmpleadoResponse {
        val estado = getEstado(id)
        val newName = request.nombre.trim()
        if (!estado.nombre.equals(newName, ignoreCase = true) && estadoEmpleadoRepository.existsByNombreIgnoreCase(newName)) {
            throw ConflictException("Ya existe un estado de empleado llamado '$newName'")
        }
        estado.nombre = newName
        estado.descripcion = request.descripcion?.trim()
        estado.actualizadoEn = Instant.now()
        return estadoEmpleadoRepository.save(estado).toResponse()
    }

    @Transactional
    fun deleteEstado(id: Long) {
        estadoEmpleadoRepository.delete(getEstado(id))
    }

    @Transactional(readOnly = true)
    fun findAllPuestos(): List<EmpleadoPuestoResponse> {
        return empleadoPuestoRepository.findAll().map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun findPuestoById(id: Long): EmpleadoPuestoResponse = getPuesto(id).toResponse()

    @Transactional
    fun createPuesto(request: EmpleadoPuestoRequest): EmpleadoPuestoResponse {
        val name = request.nombre.trim()
        if (empleadoPuestoRepository.existsByNombreIgnoreCase(name)) {
            throw ConflictException("Ya existe un puesto llamado '$name'")
        }
        val puesto = EmpleadoPuesto(
            nombre = name,
            descripcion = request.descripcion?.trim()
        )
        return empleadoPuestoRepository.save(puesto).toResponse()
    }

    @Transactional
    fun updatePuesto(id: Long, request: EmpleadoPuestoRequest): EmpleadoPuestoResponse {
        val puesto = getPuesto(id)
        val newName = request.nombre.trim()
        if (!puesto.nombre.equals(newName, ignoreCase = true) && empleadoPuestoRepository.existsByNombreIgnoreCase(newName)) {
            throw ConflictException("Ya existe un puesto llamado '$newName'")
        }
        puesto.nombre = newName
        puesto.descripcion = request.descripcion?.trim()
        return empleadoPuestoRepository.save(puesto).toResponse()
    }

    @Transactional
    fun deletePuesto(id: Long) {
        empleadoPuestoRepository.delete(getPuesto(id))
    }

    @Transactional(readOnly = true)
    fun findAllEmpleados(): List<EmpleadoResponse> {
        return empleadoRepository.findAll().map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun findEmpleadoById(id: Long): EmpleadoResponse = getEmpleado(id).toResponse()

    @Transactional
    fun createEmpleado(request: EmpleadoRequest): EmpleadoResponse {
        if (empleadoRepository.existsByDpi(request.dpi.trim())) {
            throw ConflictException("Ya existe un empleado con DPI '${request.dpi}'")
        }

        val empleado = Empleado(
            nombres = request.nombres.trim(),
            apellidos = request.apellidos.trim(),
            dpi = request.dpi.trim(),
            telefono = request.telefono?.trim(),
            puesto = request.empleadoPuestoId?.let { getPuesto(it) },
            estado = getEstado(request.estadoEmpleadoId),
            fechaIngreso = request.fechaIngreso,
            fotoUrl = request.fotoUrl?.trim()
        )

        return empleadoRepository.save(empleado).toResponse()
    }

    @Transactional
    fun updateEmpleado(id: Long, request: EmpleadoRequest): EmpleadoResponse {
        val empleado = getEmpleado(id)
        val dpi = request.dpi.trim()

        if (!empleado.dpi.equals(dpi, ignoreCase = true) && empleadoRepository.existsByDpi(dpi)) {
            throw ConflictException("Ya existe un empleado con DPI '$dpi'")
        }

        empleado.nombres = request.nombres.trim()
        empleado.apellidos = request.apellidos.trim()
        empleado.dpi = dpi
        empleado.telefono = request.telefono?.trim()
        empleado.estado = getEstado(request.estadoEmpleadoId)
        empleado.puesto = request.empleadoPuestoId?.let { getPuesto(it) }
        empleado.fechaIngreso = request.fechaIngreso
        empleado.fotoUrl = request.fotoUrl?.trim()
        empleado.actualizadoEn = Instant.now()

        return empleadoRepository.save(empleado).toResponse()
    }

    @Transactional
    fun deleteEmpleado(id: Long) {
        empleadoRepository.delete(getEmpleado(id))
    }

    @Transactional(readOnly = true)
    fun getEmpleado(id: Long): Empleado {
        return empleadoRepository.findById(id)
            .orElseThrow { NotFoundException("Empleado con id $id no encontrado") }
    }

    @Transactional(readOnly = true)
    fun getEstado(id: Long): EstadoEmpleado {
        return estadoEmpleadoRepository.findById(id)
            .orElseThrow { NotFoundException("Estado de empleado con id $id no encontrado") }
    }

    @Transactional(readOnly = true)
    fun getPuesto(id: Long): EmpleadoPuesto {
        return empleadoPuestoRepository.findById(id)
            .orElseThrow { NotFoundException("Puesto de empleado con id $id no encontrado") }
    }

    private fun EstadoEmpleado.toResponse() = EstadoEmpleadoResponse(
        id = id ?: 0L,
        nombre = nombre,
        descripcion = descripcion
    )

    private fun EmpleadoPuesto.toResponse() = EmpleadoPuestoResponse(
        id = id ?: 0L,
        nombre = nombre,
        descripcion = descripcion
    )

    private fun Empleado.toResponse() = EmpleadoResponse(
        id = id ?: 0L,
        nombres = nombres,
        apellidos = apellidos,
        dpi = dpi,
        telefono = telefono,
        estado = estado.toResponse(),
        puesto = puesto?.toResponse(),
        fechaIngreso = fechaIngreso,
        fotoUrl = fotoUrl
    )
}
