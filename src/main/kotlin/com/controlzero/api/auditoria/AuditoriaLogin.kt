package com.controlzero.api.auditoria

import com.controlzero.api.usuario.Usuario
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "auditoria_login")
class AuditoriaLogin(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    var usuario: Usuario? = null,

    @Column(name = "email_intento", length = 150)
    var emailIntento: String? = null,

    @Column(name = "fecha_evento", nullable = false)
    var fechaEvento: Instant = Instant.now(),

    @Column(nullable = false)
    var exito: Boolean,

    @Column(name = "ip_address", length = 45)
    var ipAddress: String? = null,

    @Column(name = "user_agent", length = 255)
    var userAgent: String? = null,

    @Column(length = 255)
    var detalle: String? = null
)
