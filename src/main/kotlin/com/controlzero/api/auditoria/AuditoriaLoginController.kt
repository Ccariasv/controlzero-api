package com.controlzero.api.auditoria

import com.controlzero.api.auditoria.dto.AuditoriaLoginResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auditoria/login")
@SecurityRequirement(name = "bearerAuth")
class AuditoriaLoginController(
    private val auditoriaLoginService: AuditoriaLoginService
) {

    @GetMapping
    fun findAll(): List<AuditoriaLoginResponse> = auditoriaLoginService.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): AuditoriaLoginResponse = auditoriaLoginService.findById(id)
}
