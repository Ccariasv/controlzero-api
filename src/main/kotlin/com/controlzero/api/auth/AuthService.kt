package com.controlzero.api.auth

import com.controlzero.api.auditoria.AuditoriaLoginService
import com.controlzero.api.auth.dto.AuthTokensResponse
import com.controlzero.api.auth.dto.AuthUserResponse
import com.controlzero.api.auth.dto.LoginRequest
import com.controlzero.api.auth.dto.RefreshTokenRequest
import com.controlzero.api.auth.dto.RegisterRequest
import com.controlzero.api.auth.dto.RegisterResponse
import com.controlzero.api.common.exception.ConflictException
import com.controlzero.api.common.exception.UnauthorizedException
import com.controlzero.api.empleado.EmpleadoService
import com.controlzero.api.empleado.dto.EmpleadoRequest
import com.controlzero.api.rol.RolService
import com.controlzero.api.security.JwtTokenService
import com.controlzero.api.security.JwtUserSnapshot
import com.controlzero.api.usuario.Usuario
import com.controlzero.api.usuario.UsuarioRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class AuthService(
    private val usuarioRepository: UsuarioRepository,
    private val empleadoService: EmpleadoService,
    private val rolService: RolService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenService: JwtTokenService,
    private val auditoriaLoginService: AuditoriaLoginService
) {

    @Transactional
    fun register(request: RegisterRequest): RegisterResponse {
        val normalizedEmail = request.email.trim().lowercase()

        if (usuarioRepository.existsByEmail(normalizedEmail)) {
            throw ConflictException("Ya existe un usuario con email '${request.email}'")
        }

        val empleado = empleadoService.createEmpleado(
            EmpleadoRequest(
                nombres = request.nombres,
                apellidos = request.apellidos,
                dpi = request.dpi,
                telefono = request.telefono,
                empleadoPuestoId = request.empleadoPuestoId,
                estadoEmpleadoId = request.estadoEmpleadoId,
                fechaIngreso = request.fechaIngreso,
                fotoUrl = request.fotoUrl
            )
        )

        val empleadoEntity = empleadoService.getEmpleado(empleado.id)
        val rol = rolService.getRol(request.rolId)

        val usuario = usuarioRepository.save(
            Usuario(
                empleado = empleadoEntity,
                rol = rol,
                email = normalizedEmail,
                passwordHash = passwordEncoder.encode(request.password)
                    ?: throw IllegalStateException("No se pudo codificar el password"),
                activo = true
            )
        )

        return RegisterResponse(
            usuario = AuthUserResponse(
                usuarioId = usuario.id ?: 0L,
                empleadoId = usuario.empleado.id ?: 0L,
                email = usuario.email,
                rol = usuario.rol.nombre
            )
        )
    }

    @Transactional
    fun login(request: LoginRequest, httpRequest: HttpServletRequest): AuthTokensResponse {
        val email = request.email.trim().lowercase()
        val userAgent = httpRequest.getHeader("User-Agent")
        val ipAddress = httpRequest.remoteAddr

        val usuario = usuarioRepository.findByEmail(email).orElse(null)
        if (usuario == null) {
            auditoriaLoginService.saveLoginAttempt(
                usuario = null,
                emailIntento = email,
                exito = false,
                ipAddress = ipAddress,
                userAgent = userAgent,
                detalle = "Usuario no encontrado"
            )
            throw UnauthorizedException("Credenciales inválidas")
        }

        if (!usuario.activo) {
            auditoriaLoginService.saveLoginAttempt(
                usuario = usuario,
                emailIntento = email,
                exito = false,
                ipAddress = ipAddress,
                userAgent = userAgent,
                detalle = "Usuario inactivo"
            )
            throw UnauthorizedException("Usuario inactivo")
        }

        if (!passwordEncoder.matches(request.password, usuario.passwordHash)) {
            auditoriaLoginService.saveLoginAttempt(
                usuario = usuario,
                emailIntento = email,
                exito = false,
                ipAddress = ipAddress,
                userAgent = userAgent,
                detalle = "Password inválido"
            )
            throw UnauthorizedException("Credenciales inválidas")
        }

        usuario.ultimoLogin = Instant.now()
        usuarioRepository.save(usuario)

        auditoriaLoginService.saveLoginAttempt(
            usuario = usuario,
            emailIntento = email,
            exito = true,
            ipAddress = ipAddress,
            userAgent = userAgent,
            detalle = "Login exitoso"
        )

        return createTokensForUser(usuario)
    }

    @Transactional(readOnly = true)
    fun refresh(request: RefreshTokenRequest): AuthTokensResponse {
        val claims = jwtTokenService.parseRefreshToken(request.refreshToken)
        val email = claims.subject ?: throw UnauthorizedException("Refresh token inválido")
        val user = usuarioRepository.findByEmail(email.lowercase())
            .orElseThrow { UnauthorizedException("Usuario no encontrado para refresh token") }

        if (!user.activo) {
            throw UnauthorizedException("Usuario inactivo")
        }

        return createTokensForUser(user)
    }

    private fun createTokensForUser(usuario: Usuario): AuthTokensResponse {
        val permissions = usuario.rol.permisos.map { it.nombre }.sorted()
        val snapshot = JwtUserSnapshot(
            userId = usuario.id ?: 0L,
            email = usuario.email,
            roleName = usuario.rol.nombre,
            permissions = permissions
        )

        return AuthTokensResponse(
            sessionToken = jwtTokenService.generateSessionToken(snapshot),
            refreshToken = jwtTokenService.generateRefreshToken(snapshot),
            expiresInSeconds = jwtTokenService.sessionExpiresInSeconds()
        )
    }
}
