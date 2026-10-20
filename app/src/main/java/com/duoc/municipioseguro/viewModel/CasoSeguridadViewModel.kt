package com.duoc.municipioseguro.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.duoc.municipioseguro.Model.CasoSeguridad
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
}