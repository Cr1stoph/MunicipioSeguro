package com.duoc.municipioseguro.viewModel

import androidx.lifecycle.ViewModel
import com.duoc.municipioseguro.repository.AuthRepository
import com.duoc.municipioseguro.repository.CasoSeguridadRepository

class CasoSeguridadViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    private val casoRepository = CasoSeguridadRepository()
}