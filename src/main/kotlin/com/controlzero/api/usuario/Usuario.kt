package com.controlzero.api.usuario

import com.controlzero.api.empleado.Empleado
import com.controlzero.api.rol.Rol
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "usuario")
class Usuario(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "empleado_id", nullable = false, unique = true)
    var empleado: Empleado,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id", nullable = false)
    var rol: Rol,

    @Column(nullable = false, unique = true, length = 150)
    var email: String,

    @Column(name = "password_hash", nullable = false, length = 255)
    var passwordHash: String,

    @Column(nullable = false)
    var activo: Boolean = true,

    @Column(name = "ultimo_login")
    var ultimoLogin: Instant? = null,

    @Column(name = "creado_en", nullable = false)
    var creadoEn: Instant = Instant.now(),

    @Column(name = "actualizado_en", nullable = false)
    var actualizadoEn: Instant = Instant.now()
)
