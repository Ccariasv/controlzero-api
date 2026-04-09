package com.controlzero.api.usuario

import com.controlzero.api.common.exception.ConflictException
import com.controlzero.api.common.exception.NotFoundException
import com.controlzero.api.empleado.EmpleadoService
import com.controlzero.api.rol.RolService
import com.controlzero.api.usuario.dto.UsuarioRequest
import com.controlzero.api.usuario.dto.UsuarioResponse
import com.controlzero.api.usuario.dto.UsuarioUpdateRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class UsuarioService(
    private val usuarioRepository: UsuarioRepository,
    private val empleadoService: EmpleadoService,
    private val rolService: RolService,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional(readOnly = true)
    fun findAll(): List<UsuarioResponse> = usuarioRepository.findAll().map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): UsuarioResponse = getUsuario(id).toResponse()

    @Transactional
    fun create(request: UsuarioRequest): UsuarioResponse {
        validateEmailAvailability(request.email)
        validateEmpleadoAvailability(request.empleadoId)

        val usuario = Usuario(
            empleado = empleadoService.getEmpleado(request.empleadoId),
            rol = rolService.getRol(request.rolId),
            email = request.email.trim().lowercase(),
            passwordHash = passwordEncoder.encode(request.password)
                ?: throw IllegalStateException("No se pudo codificar el password"),
            activo = request.activo
        )

        return usuarioRepository.save(usuario).toResponse()
    }

    @Transactional
    fun update(id: Long, request: UsuarioUpdateRequest): UsuarioResponse {
        val usuario = getUsuario(id)

        request.email?.trim()?.lowercase()?.let { newEmail ->
            if (!usuario.email.equals(newEmail, ignoreCase = true)) {
                validateEmailAvailability(newEmail)
                usuario.email = newEmail
            }
        }

        request.rolId?.let { usuario.rol = rolService.getRol(it) }
        request.password?.let {
            usuario.passwordHash = passwordEncoder.encode(it)
                ?: throw IllegalStateException("No se pudo codificar el password")
        }
        request.activo?.let { usuario.activo = it }
        usuario.actualizadoEn = Instant.now()

        return usuarioRepository.save(usuario).toResponse()
    }

    @Transactional
    fun delete(id: Long) {
        usuarioRepository.delete(getUsuario(id))
    }

    @Transactional(readOnly = true)
    fun getUsuario(id: Long): Usuario {
        return usuarioRepository.findById(id)
            .orElseThrow { NotFoundException("Usuario con id $id no encontrado") }
    }

    @Transactional(readOnly = true)
    fun getUsuarioByEmail(email: String): Usuario {
        return usuarioRepository.findByEmail(email.trim().lowercase())
            .orElseThrow { NotFoundException("Usuario no encontrado") }
    }

    private fun validateEmailAvailability(email: String) {
        if (usuarioRepository.existsByEmail(email.trim().lowercase())) {
            throw ConflictException("Ya existe un usuario con email '$email'")
        }
    }

    private fun validateEmpleadoAvailability(empleadoId: Long) {
        if (usuarioRepository.existsByEmpleadoId(empleadoId)) {
            throw ConflictException("El empleado con id $empleadoId ya tiene usuario")
        }
    }

    private fun Usuario.toResponse() = UsuarioResponse(
        id = id ?: 0L,
        empleadoId = empleado.id ?: 0L,
        empleadoNombre = "${empleado.nombres} ${empleado.apellidos}",
        rolId = rol.id ?: 0L,
        rolNombre = rol.nombre,
        email = email,
        activo = activo,
        ultimoLogin = ultimoLogin
    )
}
