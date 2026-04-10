package com.controlzero.api.empleado

import com.controlzero.api.empleado.dto.EmpleadoPuestoRequest
import com.controlzero.api.empleado.dto.EmpleadoPuestoResponse
import com.controlzero.api.empleado.dto.EmpleadoRequest
import com.controlzero.api.empleado.dto.EmpleadoResponse
import com.controlzero.api.empleado.dto.EstadoEmpleadoRequest
import com.controlzero.api.empleado.dto.EstadoEmpleadoResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/empleados")
@SecurityRequirement(name = "bearerAuth")
class EmpleadoController(
    private val empleadoService: EmpleadoService
) {

    @GetMapping("/estados")
    fun findAllEstados(): List<EstadoEmpleadoResponse> = empleadoService.findAllEstados()

    @GetMapping("/estados/{id}")
    fun findEstadoById(@PathVariable id: Long): EstadoEmpleadoResponse = empleadoService.findEstadoById(id)

    @PostMapping("/estados")
    @ResponseStatus(HttpStatus.CREATED)
    fun createEstado(@Valid @RequestBody request: EstadoEmpleadoRequest): EstadoEmpleadoResponse {
        return empleadoService.createEstado(request)
    }

    @PutMapping("/estados/{id}")
    fun updateEstado(@PathVariable id: Long, @Valid @RequestBody request: EstadoEmpleadoRequest): EstadoEmpleadoResponse {
        return empleadoService.updateEstado(id, request)
    }

    @DeleteMapping("/estados/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteEstado(@PathVariable id: Long) {
        empleadoService.deleteEstado(id)
    }

    @GetMapping("/puestos")
    fun findAllPuestos(): List<EmpleadoPuestoResponse> = empleadoService.findAllPuestos()

    @GetMapping("/puestos/{id}")
    fun findPuestoById(@PathVariable id: Long): EmpleadoPuestoResponse = empleadoService.findPuestoById(id)

    @PostMapping("/puestos")
    @ResponseStatus(HttpStatus.CREATED)
    fun createPuesto(@Valid @RequestBody request: EmpleadoPuestoRequest): EmpleadoPuestoResponse {
        return empleadoService.createPuesto(request)
    }

    @PutMapping("/puestos/{id}")
    fun updatePuesto(@PathVariable id: Long, @Valid @RequestBody request: EmpleadoPuestoRequest): EmpleadoPuestoResponse {
        return empleadoService.updatePuesto(id, request)
    }

    @DeleteMapping("/puestos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletePuesto(@PathVariable id: Long) {
        empleadoService.deletePuesto(id)
    }

    @GetMapping
    fun findAllEmpleados(): List<EmpleadoResponse> = empleadoService.findAllEmpleados()

    @GetMapping("/{id}")
    fun findEmpleadoById(@PathVariable id: Long): EmpleadoResponse = empleadoService.findEmpleadoById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createEmpleado(@Valid @RequestBody request: EmpleadoRequest): EmpleadoResponse {
        return empleadoService.createEmpleado(request)
    }

    @PutMapping("/{id}")
    fun updateEmpleado(@PathVariable id: Long, @Valid @RequestBody request: EmpleadoRequest): EmpleadoResponse {
        return empleadoService.updateEmpleado(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteEmpleado(@PathVariable id: Long) {
        empleadoService.deleteEmpleado(id)
    }
}
