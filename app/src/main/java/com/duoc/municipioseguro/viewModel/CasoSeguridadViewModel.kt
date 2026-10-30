package com.duoc.municipioseguro.viewModel

import android.app.Application
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.municipioseguro.Model.CasoSeguridad
import com.duoc.municipioseguro.Model.CategoriaCaso
import com.duoc.municipioseguro.Model.Estado
import com.duoc.municipioseguro.Model.Observacion
import com.duoc.municipioseguro.Model.Prioridad
import com.duoc.municipioseguro.Model.SesionUsuario
import com.duoc.municipioseguro.Model.UsuarioRol
import com.duoc.municipioseguro.repository.AuthRepository
import com.duoc.municipioseguro.repository.CasoSeguridadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class CasoSeguridadViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository = AuthRepository(application)
    private val casoRepository = CasoSeguridadRepository(application)

    private val _listaCasos = MutableStateFlow<List<CasoSeguridad>>(emptyList())
    val listaCasos: StateFlow<List<CasoSeguridad>> = _listaCasos.asStateFlow()

    private val _sesionActual = MutableStateFlow<SesionUsuario?>(authRepository.obtenerSesionActual())
    val sesionActual: StateFlow<SesionUsuario?> = _sesionActual.asStateFlow()

    init {
        actualizarListaCasos()
    }

    private fun actualizarListaCasos() {
        viewModelScope.launch {
            _listaCasos.value = casoRepository.obtenerTodosLosCasos()
        }
    }

    fun iniciarSesionConRoom(
        correo: String,
        contrasena: String,
        onResultado: (exito: Boolean, mensaje: String) -> Unit
    ) {
        viewModelScope.launch {
            if (correo.isBlank() || contrasena.isBlank()) {
                onResultado(false, "Por favor completa el correo y la contraseña")
                return@launch
            }
            val sesion = authRepository.validarCredencialesRoom(correo, contrasena)
            if (sesion != null) {
                _sesionActual.value = sesion
                onResultado(true, "Bienvenido/a, ${sesion.nombreGenerico}")
            } else {
                onResultado(false, "Credenciales incorrectas o usuario no registrado")
            }
        }
    }

    fun registrarUsuarioConRoom(
        correo: String,
        contrasena: String,
        rol: UsuarioRol,
        onResultado: (exito: Boolean, mensaje: String) -> Unit
    ) {
        viewModelScope.launch {
            if (correo.isBlank() || contrasena.isBlank()) {
                onResultado(false, "Ingresa un correo y contraseña válidos")
                return@launch
            }
            val exito = authRepository.registrarUsuarioRoom(correo, contrasena, rol)
            if (exito) {
                onResultado(true, "Usuario registrado exitosamente en Room")
            } else {
                onResultado(false, "El correo $correo ya se encuentra registrado")
            }
        }
    }

    fun iniciarSesionSimulada(rol: UsuarioRol) {
        val sesion = authRepository.iniciarSesionSimulada(rol)
        _sesionActual.value = sesion
    }

    fun cerrarSesion() {
        authRepository.cerrarSesion()
        _sesionActual.value = null
    }

    fun registrarCaso(
        id: String,
        categoria: CategoriaCaso,
        ubicacion: String,
        descripcion: String,
        prioridad: Prioridad,
        latitud: Double? = null,
        longitud: Double? = null,
        urlEvidencia: String? = null
    ) {
        viewModelScope.launch {
            val (latFinal, lngFinal) = if (latitud != null && longitud != null) {
                Pair(latitud, longitud)
            } else {
                geocodificarUbicacion(ubicacion)
            }

            val nuevoCaso = CasoSeguridad(
                id = id,
                categoria = categoria,
                fechaHora = System.currentTimeMillis(),
                ubicacion = ubicacion,
                descripcion = descripcion,
                prioridad = prioridad,
                estado = Estado.Registrado,
                rolAsignado = null,
                observaciones = emptyList(),
                urlEvidencia = urlEvidencia,
                latitud = latFinal,
                longitud = lngFinal,
                autorEmail = _sesionActual.value?.nombreGenerico ?: "vecino@sanjoaquin.cl"
            )
            casoRepository.guardarNuevoCaso(nuevoCaso)
            actualizarListaCasos()
        }
    }

    private fun geocodificarUbicacion(ubicacionTexto: String): Pair<Double, Double> {
        val baseLat = -33.4883
        val baseLng = -70.6333

        return try {
            val geocoder = Geocoder(getApplication(), Locale.getDefault())
            val direccionBusqueda = "$ubicacionTexto, San Joaquín, Santiago, Chile"
            @Suppress("DEPRECATION")
            val resultados = geocoder.getFromLocationName(direccionBusqueda, 1)

            if (!resultados.isNullOrEmpty()) {
                val primeraUbicacion = resultados[0]
                Pair(primeraUbicacion.latitude, primeraUbicacion.longitude)
            } else {
                val offsetLat = (kotlin.math.abs(ubicacionTexto.hashCode()) % 50) / 5000.0
                val offsetLng = (kotlin.math.abs(ubicacionTexto.hashCode()) % 50) / 5000.0
                Pair(baseLat + offsetLat, baseLng + offsetLng)
            }
        } catch (e: Exception) {
            val offsetLat = (kotlin.math.abs(ubicacionTexto.hashCode()) % 50) / 5000.0
            val offsetLng = (kotlin.math.abs(ubicacionTexto.hashCode()) % 50) / 5000.0
            Pair(baseLat + offsetLat, baseLng + offsetLng)
        }
    }

    fun cambiarEstadoCaso(idCaso: String, nuevoEstado: Estado) {
        viewModelScope.launch {
            casoRepository.actualizarEstado(idCaso, nuevoEstado)
            actualizarListaCasos()
        }
    }

    fun registrarObservacion(idCaso: String, notaTexto: String, autorRol: UsuarioRol) {
        viewModelScope.launch {
            val nuevaObservacion = Observacion(
                id = "Obs-${System.currentTimeMillis()}",
                nota = notaTexto,
                fechaHora = System.currentTimeMillis(),
                autor = autorRol
            )
            casoRepository.agregarObservacion(idCaso, nuevaObservacion)
            actualizarListaCasos()
        }
    }

    fun obtenerDetalleCaso(id: String): CasoSeguridad? {
        return _listaCasos.value.find { it.id == id }
    }
}
