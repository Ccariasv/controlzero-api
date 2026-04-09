package com.controlzero.api.rol

import com.controlzero.api.permiso.Permiso
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "rol")
class Rol(
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
    var actualizadoEn: Instant = Instant.now(),

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "permiso_rol",
        joinColumns = [JoinColumn(name = "rol_id")],
        inverseJoinColumns = [JoinColumn(name = "permiso_id")]
    )
    var permisos: MutableSet<Permiso> = mutableSetOf()
)
