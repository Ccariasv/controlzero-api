package com.controlzero.api.permiso

import com.controlzero.api.permiso.dto.PermisoRequest
import com.controlzero.api.permiso.dto.PermisoResponse
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
@RequestMapping("/api/v1/permisos")
@SecurityRequirement(name = "bearerAuth")
class PermisoController(
    private val permisoService: PermisoService
) {

    @GetMapping
    fun findAll(): List<PermisoResponse> = permisoService.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): PermisoResponse = permisoService.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: PermisoRequest): PermisoResponse = permisoService.create(request)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: PermisoRequest): PermisoResponse {
        return permisoService.update(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        permisoService.delete(id)
    }
}
