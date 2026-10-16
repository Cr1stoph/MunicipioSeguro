package com.duoc.municipioseguro.Model

sealed interface UsuarioRol {
    data object Residente: UsuarioRol
    data object Operador: UsuarioRol
    data object Supervisor: UsuarioRol
    data object Administrador: UsuarioRol
}