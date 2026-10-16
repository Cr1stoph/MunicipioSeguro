package com.duoc.municipioseguro.Model

data class Observacion(
    val id: String,
    val nota: String,
    val fechaHora: Long,
    val autor: UsuarioRol
)