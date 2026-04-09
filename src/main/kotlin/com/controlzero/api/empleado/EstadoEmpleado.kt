package com.controlzero.api.empleado

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "estado_empleado")
class EstadoEmpleado(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true, length = 50)
    var nombre: String,

    @Column(length = 150)
    var descripcion: String? = null,

    @Column(name = "creado_en", nullable = false)
    var creadoEn: Instant = Instant.now(),

    @Column(name = "actualizado_en", nullable = false)
    var actualizadoEn: Instant = Instant.now()
)
