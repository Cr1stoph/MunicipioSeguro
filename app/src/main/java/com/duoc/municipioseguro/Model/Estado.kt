package com.duoc.municipioseguro.Model

sealed interface Estado {
    data object Registrado: Estado
    data object EnRevision: Estado
    data object Derivado : Estado
    data object EnSeguimiento: Estado
}