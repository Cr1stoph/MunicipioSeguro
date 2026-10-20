package com.duoc.municipioseguro.viewModel

import androidx.lifecycle.ViewModel
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

class CasoSeguridadViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    private val casoRepository = CasoSeguridadRepository()
    private var contadorObservaciones = 1

    private val _listaCasos = MutableStateFlow< List<CasoSeguridad>>(emptyList())
    val listaCasos: StateFlow<List<CasoSeguridad>> = _listaCasos.asStateFlow()

    private val _sesionActual = MutableStateFlow<SesionUsuario?>(null)
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

    fun registrarCaso(id: String, categoria: CategoriaCaso, ubicacion: String, descripcion: String, prioridad: Prioridad){
        val nuevoCaso = CasoSeguridad(
            id = id, categoria = categoria, fechaHora = System.currentTimeMillis(), ubicacion = ubicacion, descripcion = descripcion, prioridad = prioridad,
            estado = Estado.Registrado, rolAsignado = null, observaciones = emptyList(), urlEvidencia = null
        )
        casoRepository.guardarNuevoCaso(nuevoCaso)
        actualizarListaCasos()
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