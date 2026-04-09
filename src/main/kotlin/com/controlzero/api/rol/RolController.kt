package com.controlzero.api.rol

import com.controlzero.api.rol.dto.RolRequest
import com.controlzero.api.rol.dto.RolResponse
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
@RequestMapping("/api/v1/roles")
class RolController(
    private val rolService: RolService
) {

    @GetMapping
    fun findAll(): List<RolResponse> = rolService.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): RolResponse = rolService.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: RolRequest): RolResponse = rolService.create(request)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: RolRequest): RolResponse {
        return rolService.update(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        rolService.delete(id)
    }
}
