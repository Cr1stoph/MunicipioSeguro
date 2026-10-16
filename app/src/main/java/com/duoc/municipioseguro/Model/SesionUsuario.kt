package com.duoc.municipioseguro.Model

data class SesionUsuario(
    val nombreGenerico: String, //"Operador Turno Mañana" o "Vecino Prueba"
    val rol: UsuarioRol
)