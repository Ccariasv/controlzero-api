package com.controlzero.api.auth

import com.controlzero.api.auth.dto.AuthTokensResponse
import com.controlzero.api.auth.dto.LoginRequest
import com.controlzero.api.auth.dto.RefreshTokenRequest
import com.controlzero.api.auth.dto.RegisterRequest
import com.controlzero.api.auth.dto.RegisterResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@Valid @RequestBody request: RegisterRequest): RegisterResponse {
        return authService.register(request)
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest,
        httpServletRequest: HttpServletRequest
    ): AuthTokensResponse {
        return authService.login(request, httpServletRequest)
    }

    @PostMapping("/refresh")
    fun refresh(@Valid @RequestBody request: RefreshTokenRequest): AuthTokensResponse {
        return authService.refresh(request)
    }
}
