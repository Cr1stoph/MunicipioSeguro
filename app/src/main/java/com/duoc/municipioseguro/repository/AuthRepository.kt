package com.duoc.municipioseguro.repository

import com.duoc.municipioseguro.Model.*
class AuthRepository {

    // Variable privada que guarda la sesión actual en memoria.
    // Empieza en 'null' porque al abrir la app nadie ha iniciado sesión.
    private var sesionActual: SesionUsuario? = null

    // Función para simular el ingreso
    fun iniciarSesion(rolSeleccionado: UsuarioRol): SesionUsuario {
        val nuevaSesion = SesionUsuario(
            nombreGenerico = "Usuario Prueba (${rolSeleccionado.javaClass.simpleName})",
            rol = rolSeleccionado
        )
        sesionActual = nuevaSesion
        return nuevaSesion
    }

    // El ViewModel usará esto para saber qué permisos darle al usuario
    fun obtenerSesionActual(): SesionUsuario? {
        return sesionActual
    }

    fun cerrarSesion() {
        sesionActual = null
    }
}