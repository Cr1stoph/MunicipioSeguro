package com.duoc.municipioseguro.repository

import android.content.Context
import android.content.SharedPreferences
import com.duoc.municipioseguro.Model.*
import com.duoc.municipioseguro.data.local.MunicipioDatabase
import com.duoc.municipioseguro.data.local.UsuarioDao
import com.duoc.municipioseguro.data.local.UsuarioEntity
import com.duoc.municipioseguro.remote.RetrofitInstance

open class AuthRepository(context: Context) {

    private val usuarioDao: UsuarioDao = MunicipioDatabase.getDatabase(context).usuarioDao()
    private val prefs: SharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)

    private var sesionActual: SesionUsuario? = null

    init {
        // Al instanciar, intentamos recuperar la sesión previamente guardada
        val rolGuardado = prefs.getString("user_role", null)
        val correoGuardado = prefs.getString("user_email", "") ?: ""
        if (rolGuardado != null) {
            val rolEnum = obtenerRolDesdeString(rolGuardado)
            if (rolEnum != null) {
                sesionActual = SesionUsuario(
                    nombreGenerico = if (correoGuardado.isNotBlank()) correoGuardado else "Usuario (${rolEnum.javaClass.simpleName})",
                    rol = rolEnum
                )
            }
        }
    }

    // Validación de inicio de sesión consultando Room Database y sincronizando con Xano
    suspend fun validarCredencialesRoom(correo: String, contrasena: String): SesionUsuario? {
        try {
            val usuariosRemotos = RetrofitInstance.api.obtenerUsuarios()
            for (u in usuariosRemotos) {
                usuarioDao.insertarUsuario(u)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val usuarioEncontrado = usuarioDao.validarLogin(correo.trim(), contrasena.trim())
        return if (usuarioEncontrado != null) {
            val rolEnum = obtenerRolDesdeString(usuarioEncontrado.rol) ?: UsuarioRol.Residente
            val nuevaSesion = SesionUsuario(
                nombreGenerico = usuarioEncontrado.correo,
                rol = rolEnum
            )
            sesionActual = nuevaSesion
            prefs.edit()
                .putString("user_role", rolEnum.javaClass.simpleName)
                .putString("user_email", usuarioEncontrado.correo)
                .apply()
            nuevaSesion
        } else {
            null
        }
    }

    // Registro de nuevo usuario en Room Database y Xano
    suspend fun registrarUsuarioRoom(correo: String, contrasena: String, rol: UsuarioRol): Boolean {
        val existente = usuarioDao.obtenerPorCorreo(correo.trim())
        if (existente != null) {
            return false // Ya existe un usuario con este correo
        }
        val nuevoEntity = UsuarioEntity(
            correo = correo.trim(),
            contrasena = contrasena.trim(),
            rol = rol.javaClass.simpleName
        )
        // Guardar localmente
        usuarioDao.insertarUsuario(nuevoEntity)
        // Enviar a Xano en segundo plano usando Map para evitar enviar id=0
        try {
            val body = mapOf(
                "correo" to correo.trim(),
                "contrasena" to contrasena.trim(),
                "rol" to rol.javaClass.simpleName
            )
            RetrofitInstance.api.crearUsuario(body)
            android.util.Log.d("XanoSuccess", "Usuario creado en Xano exitosamente")
        } catch (e: Exception) {
            android.util.Log.e("XanoError", "Error al crear usuario en Xano: ${e.localizedMessage}", e)
        }
        return true
    }

    // Función de simulación mantenida para retrocompatibilidad
    fun iniciarSesionSimulada(rolSeleccionado: UsuarioRol): SesionUsuario {
        val nuevaSesion = SesionUsuario(
            nombreGenerico = "Usuario Prueba (${rolSeleccionado.javaClass.simpleName})",
            rol = rolSeleccionado
        )
        sesionActual = nuevaSesion
        prefs.edit().putString("user_role", rolSeleccionado.javaClass.simpleName).apply()
        return nuevaSesion
    }

    open fun obtenerSesionActual(): SesionUsuario? {
        return sesionActual
    }

    fun cerrarSesion() {
        sesionActual = null
        prefs.edit().remove("user_role").remove("user_email").apply()
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
