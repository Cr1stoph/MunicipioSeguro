package com.duoc.municipioseguro.viewModel

import android.app.Application
import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
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
import java.util.Locale

class CasoSeguridadViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository = AuthRepository(application)
    private val casoRepository = CasoSeguridadRepository()
    private var contadorObservaciones = 1

    private val _listaCasos = MutableStateFlow< List<CasoSeguridad>>(emptyList())
    val listaCasos: StateFlow<List<CasoSeguridad>> = _listaCasos.asStateFlow()

    private val _sesionActual = MutableStateFlow<SesionUsuario?>(authRepository.obtenerSesionActual())
    val sesionActual: StateFlow<SesionUsuario?> = _sesionActual.asStateFlow()

    init {
        actualizarListaCasos()
    }

    private fun actualizarListaCasos(){
        _listaCasos.value = casoRepository.obtenerTodosLosCasos()
    }

    fun iniciarSesionSimulada(rol: UsuarioRol){
        val sesion = authRepository.iniciarSesion(rol)
        _sesionActual.value = sesion
    }

    fun cerrarSesion(){
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
        longitud: Double? = null
    ) {
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
            urlEvidencia = null,
            latitud = latFinal,
            longitud = lngFinal
        )
        casoRepository.guardarNuevoCaso(nuevoCaso)
        actualizarListaCasos()
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
                // Fallback: Generar coordenadas relativas a San Joaquín basadas en el texto
                val offsetLat = (kotlin.math.abs(ubicacionTexto.hashCode()) % 50) / 5000.0
                val offsetLng = (kotlin.math.abs(ubicacionTexto.hashCode()) % 50) / 5000.0
                Pair(baseLat + offsetLat, baseLng + offsetLng)
            }
        } catch (e: Exception) {
            // Fallback en caso de error de red o geocodificación
            val offsetLat = (kotlin.math.abs(ubicacionTexto.hashCode()) % 50) / 5000.0
            val offsetLng = (kotlin.math.abs(ubicacionTexto.hashCode()) % 50) / 5000.0
            Pair(baseLat + offsetLat, baseLng + offsetLng)
        }
    }

    fun cambiarEstadoCaso(idCaso: String, nuevoEstado: Estado){
        casoRepository.actualizarEstado(idCaso, nuevoEstado)
        actualizarListaCasos()
    }

    fun registrarObservacion(idCaso: String, notaTexto: String, autorRol: UsuarioRol){
        val nuevaObservacion = Observacion(
            id = "Obs-${contadorObservaciones++}", nota = notaTexto, fechaHora = System.currentTimeMillis(), autor = autorRol
        )
        casoRepository.agregarObservacion(idCaso, nuevaObservacion)
        actualizarListaCasos()
    }

    fun obtenerDetalleCaso(id: String): CasoSeguridad?{
        return casoRepository.obtenerCasoPorId(id)
    }
}