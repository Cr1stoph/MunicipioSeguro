package com.duoc.municipioseguro.Model

sealed interface CategoriaCaso {
    data object AlumbradoPublico : CategoriaCaso
    data object EspacioPublico : CategoriaCaso
    data object ConvivenciaVecinal : CategoriaCaso
    data object OtraSituacion : CategoriaCaso
}