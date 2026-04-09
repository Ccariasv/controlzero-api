package com.controlzero.api.usuario

import com.controlzero.api.usuario.dto.UsuarioRequest
import com.controlzero.api.usuario.dto.UsuarioResponse
import com.controlzero.api.usuario.dto.UsuarioUpdateRequest
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
@RequestMapping("/api/v1/usuarios")
class UsuarioController(
    private val usuarioService: UsuarioService
) {

    @GetMapping
    fun findAll(): List<UsuarioResponse> = usuarioService.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): UsuarioResponse = usuarioService.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: UsuarioRequest): UsuarioResponse = usuarioService.create(request)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: UsuarioUpdateRequest): UsuarioResponse {
        return usuarioService.update(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        usuarioService.delete(id)
    }
}
