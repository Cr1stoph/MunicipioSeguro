package com.duoc.municipioseguro.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "casos")
data class CasoEntity(
    @PrimaryKey
    val id: String,
    val categoria: String,
    val fechaHora: Long,
    val ubicacion: String,
    val descripcion: String,
    val prioridad: String,
    val estado: String,
    val rolAsignado: String?,
    val urlEvidencia: String?,
    val latitud: Double,
    val longitud: Double,
    val autorEmail: String?,
    val observacionesJson: String
)
