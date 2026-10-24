package com.duoc.municipioseguro.repository

import android.content.Context
import android.content.SharedPreferences
import com.duoc.municipioseguro.Model.*

class AuthRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)
    
    // Variable privada que guarda la sesión actual en memoria.
    private var sesionActual: SesionUsuario? = null

    init {
        // Al instanciar, intentamos recuperar la sesión
        val rolGuardado = prefs.getString("user_role", null)
        if (rolGuardado != null) {
            val rolEnum = obtenerRolDesdeString(rolGuardado)
            if (rolEnum != null) {
                sesionActual = SesionUsuario(
                    nombreGenerico = "Usuario Guardado (${rolEnum.javaClass.simpleName})",
                    rol = rolEnum
                )
            }
        }
    }

    // Función para simular el ingreso
    fun iniciarSesion(rolSeleccionado: UsuarioRol): SesionUsuario {
        val nuevaSesion = SesionUsuario(
            nombreGenerico = "Usuario Prueba (${rolSeleccionado.javaClass.simpleName})",
            rol = rolSeleccionado
        )
        sesionActual = nuevaSesion
        // Guardamos el rol en SharedPreferences
        prefs.edit().putString("user_role", rolSeleccionado.javaClass.simpleName).apply()
        
        return nuevaSesion
    }

    // El ViewModel usará esto para saber qué permisos darle al usuario
    fun obtenerSesionActual(): SesionUsuario? {
        return sesionActual
    }

    fun cerrarSesion() {
        sesionActual = null
        prefs.edit().remove("user_role").apply()
    }
    
    private fun obtenerRolDesdeString(rolString: String): UsuarioRol? {
        return when (rolString) {
            "Residente" -> UsuarioRol.Residente
            "Operador" -> UsuarioRol.Operador
            "Supervisor" -> UsuarioRol.Supervisor
            "Administrador" -> UsuarioRol.Administrador
            else -> null
        }
    }
}