package com.controlzero.api.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "security.jwt")
data class JwtProperties(
    var secret: String = "",
    var sessionTtlMinutes: Long = 30,
    var refreshTtlDays: Long = 7
)
