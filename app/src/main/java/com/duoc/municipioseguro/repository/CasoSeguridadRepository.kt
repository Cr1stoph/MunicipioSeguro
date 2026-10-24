package com.duoc.municipioseguro.repository
import com.duoc.municipioseguro.Model.*

class CasoSeguridadRepository {

    // Lista en memoria RAM que actúa como nuestra base de datos falsa
    private val casosBD = mutableListOf<CasoSeguridad>()

    init {
        cargarDatosFicticios()
    }

    private fun cargarDatosFicticios() {
        // Fechas fijas aproximadas en milisegundos para coincidir con el diseño (15 oct 2026, 13 oct 2026, 8 oct 2026)
        // 1792022400000L es aprox octubre de 2026
        casosBD.add(
            CasoSeguridad(
                id = "SJ-2026-0148",
                categoria = CategoriaCaso.AlumbradoPublico,
                fechaHora = 1792022400000L - (1L * 86400000L), // 15 oct 2026 aprox
                ubicacion = "Av. Departamental con Las Industrias",
                descripcion = "Reporte ingresado para revisión.",
                prioridad = Prioridad.Media,
                estado = Estado.Registrado,
                rolAsignado = null
            )
        )

        casosBD.add(
            CasoSeguridad(
                id = "SJ-2026-0142",
                categoria = CategoriaCaso.EspacioPublico,
                fechaHora = 1792022400000L - (3L * 86400000L), // 13 oct 2026 aprox
                ubicacion = "Las Industrias con Pintor Cicarelli",
                descripcion = "Derivado al área de espacio público.",
                prioridad = Prioridad.Alta,
                estado = Estado.EnRevision,
                rolAsignado = UsuarioRol.Operador
            )
        )

        casosBD.add(
            CasoSeguridad(
                id = "SJ-2026-0126",
                categoria = CategoriaCaso.AlumbradoPublico,
                fechaHora = 1792022400000L - (8L * 86400000L), // 8 oct 2026 aprox
                ubicacion = "Av. Departamental con Santa Rosa",
                descripcion = "Reparación informada por el área a cargo.",
                prioridad = Prioridad.Baja,
                estado = Estado.EnSeguimiento, // O interpretado como Resuelto
                rolAsignado = UsuarioRol.Supervisor
            )
        )
    }

    // --- MÉTODOS PARA EL VIEWMODEL ---

    // Obtiene toda la lista (Para la pantalla principal)
    fun obtenerTodosLosCasos(): List<CasoSeguridad> {
        return casosBD.toList()
    }

    // Obtiene un solo caso por su ID (Para la pantalla de Detalle)
    fun obtenerCasoPorId(id: String): CasoSeguridad? {
        return casosBD.find { it.id == id }
    }

    // Guarda un nuevo reporte (Para la pantalla de Formulario)
    fun guardarNuevoCaso(nuevoCaso: CasoSeguridad) {
        casosBD.add(nuevoCaso)
    }

    // Permite al Operador/Supervisor cambiar el estado (Ej: De Registrado a En Seguimiento)
    fun actualizarEstado(id: String, nuevoEstado: Estado) {
        val index = casosBD.indexOfFirst { it.id == id }
        if (index != -1) {
            val casoAntiguo = casosBD[index]
            casosBD[index] = casoAntiguo.copy(estado = nuevoEstado)
        }
    }

    // Agrega una observación al historial del caso
    fun agregarObservacion(idCaso: String, observacion: Observacion) {
        val index = casosBD.indexOfFirst { it.id == idCaso }
        if (index != -1) {
            val casoAntiguo = casosBD[index]
            val nuevasObservaciones = casoAntiguo.observaciones.toMutableList()
            nuevasObservaciones.add(observacion)

            casosBD[index] = casoAntiguo.copy(observaciones = nuevasObservaciones)
        }
    }
}