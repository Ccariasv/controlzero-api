package com.controlzero.api.security

import com.controlzero.api.common.exception.UnauthorizedException
import io.jsonwebtoken.Claims
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtTokenService: JwtTokenService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (authHeader.isNullOrBlank() || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.substringAfter("Bearer ").trim()

        try {
            val claims = jwtTokenService.parseSessionToken(token)
            val authentication = buildAuthentication(claims, request)
            SecurityContextHolder.getContext().authentication = authentication
        } catch (_: UnauthorizedException) {
            SecurityContextHolder.clearContext()
        }

        filterChain.doFilter(request, response)
    }

    private fun buildAuthentication(
        claims: Claims,
        request: HttpServletRequest
    ): UsernamePasswordAuthenticationToken {
        val role = claims["rol"] as? String
        val permissionValues = claims["perms"] as? Collection<*>

        val authorities = mutableListOf<SimpleGrantedAuthority>()
        if (!role.isNullOrBlank()) {
            authorities.add(SimpleGrantedAuthority("ROLE_${role.uppercase()}"))
        }

        permissionValues
            ?.mapNotNull { it?.toString() }
            ?.forEach { authorities.add(SimpleGrantedAuthority(it)) }

        val authentication = UsernamePasswordAuthenticationToken(
            claims.subject,
            null,
            authorities
        )
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        return authentication
    }
}
