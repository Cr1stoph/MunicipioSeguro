package com.duoc.municipioseguro.Model

sealed interface CategoriaCaso {
    data object RoboOAsalto : CategoriaCaso
    data object RuidoMolesto : CategoriaCaso
    data object AlumbradoDefectuoso : CategoriaCaso
    data object VehiculoSospechoso : CategoriaCaso
    data object ConsumoDrogas : CategoriaCaso
    data object Otro : CategoriaCaso
}