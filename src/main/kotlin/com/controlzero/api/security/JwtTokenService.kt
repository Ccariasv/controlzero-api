package com.controlzero.api.security

import com.controlzero.api.config.JwtProperties
import com.controlzero.api.common.exception.UnauthorizedException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

@Service
class JwtTokenService(
    private val jwtProperties: JwtProperties
) {
    companion object {
        private const val TYPE_CLAIM = "typ"
        private const val TYPE_SESSION = "SESSION"
        private const val TYPE_REFRESH = "REFRESH"
    }

    fun generateSessionToken(user: JwtUserSnapshot): String {
        val now = Instant.now()
        val expiration = now.plus(jwtProperties.sessionTtlMinutes, ChronoUnit.MINUTES)

        return Jwts.builder()
            .subject(user.email)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiration))
            .claim("uid", user.userId)
            .claim("rol", user.roleName)
            .claim("perms", user.permissions)
            .claim(TYPE_CLAIM, TYPE_SESSION)
            .signWith(signingKey(), Jwts.SIG.HS256)
            .compact()
    }

    fun generateRefreshToken(user: JwtUserSnapshot): String {
        val now = Instant.now()
        val expiration = now.plus(jwtProperties.refreshTtlDays, ChronoUnit.DAYS)

        return Jwts.builder()
            .subject(user.email)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiration))
            .claim("uid", user.userId)
            .claim(TYPE_CLAIM, TYPE_REFRESH)
            .signWith(signingKey(), Jwts.SIG.HS256)
            .compact()
    }

    fun parseSessionToken(token: String): Claims {
        val claims = parse(token)
        if (claims[TYPE_CLAIM] != TYPE_SESSION) {
            throw UnauthorizedException("Token de sesión inválido")
        }
        return claims
    }

    fun parseRefreshToken(token: String): Claims {
        val claims = parse(token)
        if (claims[TYPE_CLAIM] != TYPE_REFRESH) {
            throw UnauthorizedException("Refresh token inválido")
        }
        return claims
    }

    fun sessionExpiresInSeconds(): Long = jwtProperties.sessionTtlMinutes * 60

    private fun parse(token: String): Claims {
        return try {
            Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (ex: JwtException) {
            throw UnauthorizedException("Token inválido o expirado")
        } catch (ex: IllegalArgumentException) {
            throw UnauthorizedException("Token inválido")
        }
    }

    private fun signingKey() = Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray(StandardCharsets.UTF_8))
}

data class JwtUserSnapshot(
    val userId: Long,
    val email: String,
    val roleName: String,
    val permissions: List<String>
)
