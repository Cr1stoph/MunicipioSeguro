package com.duoc.municipioseguro.Model

data class CasoSeguridad(
    val id: String,
    val categoria: CategoriaCaso,
    val fechaHora: Long,
    val ubicacion: String,
    val descripcion: String,
    val prioridad: Prioridad,
    val estado: Estado,
    val rolAsignado: UsuarioRol?,
    val observaciones: List<Observacion> = emptyList(),
    val urlEvidencia: String? = null,
    val latitud: Double = -33.4883,
    val longitud: Double = -70.6333
)