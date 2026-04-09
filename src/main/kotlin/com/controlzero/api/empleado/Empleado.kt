package com.controlzero.api.empleado

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
import java.time.LocalDate

@Entity
@Table(name = "empleado")
class Empleado(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 100)
    var nombres: String,

    @Column(nullable = false, length = 100)
    var apellidos: String,

    @Column(nullable = false, unique = true, length = 30)
    var dpi: String,

    @Column(length = 20)
    var telefono: String? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "empleado_puesto_id")
    var puesto: EmpleadoPuesto? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estado_empleado_id", nullable = false)
    var estado: EstadoEmpleado,

    @Column(name = "fecha_ingreso", nullable = false)
    var fechaIngreso: LocalDate,

    @Column(name = "foto_url", length = 255)
    var fotoUrl: String? = null,

    @Column(name = "creado_en", nullable = false)
    var creadoEn: Instant = Instant.now(),

    @Column(name = "actualizado_en", nullable = false)
    var actualizadoEn: Instant = Instant.now()
)
