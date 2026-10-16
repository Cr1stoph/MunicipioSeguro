package com.duoc.municipioseguro.Model

sealed interface Prioridad {
    data object Alta: Prioridad
    data object Media: Prioridad
    data object Baja: Prioridad
}