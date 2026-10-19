package com.duoc.municipioseguro.repository
import com.duoc.municipioseguro.Model.*

class CasoSeguridadRepository {

    // Lista en memoria RAM que actúa como nuestra base de datos falsa
    private val casosBD = mutableListOf<CasoSeguridad>()

    init {
        cargarDatosFicticios()
    }

    private fun cargarDatosFicticios() {
        casosBD.add(
            CasoSeguridad(
                id = "CASO-001",
                categoria = CategoriaCaso.AlumbradoPublico,
                fechaHora = System.currentTimeMillis() - 86400000, // Hace 1 día
                ubicacion = "Avenida Las Industrias 1200", // Sector referencial
                descripcion = "Poste chocado, cables expuestos y sin luz.",
                prioridad = Prioridad.Alta,
                estado = Estado.EnRevision,
                rolAsignado = UsuarioRol.Operador
            )
        )

        casosBD.add(
            CasoSeguridad(
                id = "CASO-002",
                categoria = CategoriaCaso.ConvivenciaVecinal,
                fechaHora = System.currentTimeMillis() - 3600000, // Hace 1 hora
                ubicacion = "Plaza Central", // Sector referencial
                descripcion = "Grupo de personas haciendo ruido excesivo.",
                prioridad = Prioridad.Baja,
                estado = Estado.Registrado,
                rolAsignado = null
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