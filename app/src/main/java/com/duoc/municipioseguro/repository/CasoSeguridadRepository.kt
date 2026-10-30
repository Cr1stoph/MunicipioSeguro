package com.duoc.municipioseguro.repository

import android.content.Context
import com.duoc.municipioseguro.Model.*
import com.duoc.municipioseguro.data.local.CasoDao
import com.duoc.municipioseguro.data.local.CasoEntity
import com.duoc.municipioseguro.data.local.MunicipioDatabase
import org.json.JSONArray
import org.json.JSONObject

class CasoSeguridadRepository(context: Context) {

    private val casoDao: CasoDao = MunicipioDatabase.getDatabase(context).casoDao()

    suspend fun obtenerTodosLosCasos(): List<CasoSeguridad> {
        val entities = casoDao.obtenerTodosLosCasos()
        return entities.map { entityToDomain(it) }
    }

    suspend fun obtenerCasoPorId(id: String): CasoSeguridad? {
        val entity = casoDao.obtenerCasoPorId(id)
        return entity?.let { entityToDomain(it) }
    }

    suspend fun guardarNuevoCaso(nuevoCaso: CasoSeguridad) {
        val entity = domainToEntity(nuevoCaso)
        casoDao.insertarCaso(entity)
    }

    suspend fun actualizarEstado(id: String, nuevoEstado: Estado) {
        val casoActual = casoDao.obtenerCasoPorId(id)
        if (casoActual != null) {
            val estadoStr = estadoToString(nuevoEstado)
            casoDao.actualizarEstado(id, estadoStr)
        }
    }

    suspend fun agregarObservacion(idCaso: String, observacion: Observacion) {
        val casoActual = casoDao.obtenerCasoPorId(idCaso)
        if (casoActual != null) {
            val listaObs = jsonToObservaciones(casoActual.observacionesJson).toMutableList()
            listaObs.add(observacion)
            val nuevoJson = observacionesToJson(listaObs)
            casoDao.actualizarObservaciones(idCaso, nuevoJson)
        }
    }

    // --- Mapeos ---

    private fun entityToDomain(entity: CasoEntity): CasoSeguridad {
        return CasoSeguridad(
            id = entity.id,
            categoria = stringToCategoria(entity.categoria),
            fechaHora = entity.fechaHora,
            ubicacion = entity.ubicacion,
            descripcion = entity.descripcion,
            prioridad = stringToPrioridad(entity.prioridad),
            estado = stringToEstado(entity.estado),
            rolAsignado = entity.rolAsignado?.let { stringToRol(it) },
            urlEvidencia = entity.urlEvidencia,
            latitud = entity.latitud,
            longitud = entity.longitud,
            autorEmail = entity.autorEmail,
            observaciones = jsonToObservaciones(entity.observacionesJson)
        )
    }

    private fun domainToEntity(caso: CasoSeguridad): CasoEntity {
        return CasoEntity(
            id = caso.id,
            categoria = caso.categoria.javaClass.simpleName,
            fechaHora = caso.fechaHora,
            ubicacion = caso.ubicacion,
            descripcion = caso.descripcion,
            prioridad = caso.prioridad.javaClass.simpleName,
            estado = estadoToString(caso.estado),
            rolAsignado = caso.rolAsignado?.javaClass?.simpleName,
            urlEvidencia = caso.urlEvidencia,
            latitud = caso.latitud,
            longitud = caso.longitud,
            autorEmail = caso.autorEmail,
            observacionesJson = observacionesToJson(caso.observaciones)
        )
    }

    private fun estadoToString(estado: Estado): String {
        return when (estado) {
            is Estado.Registrado -> "Registrado"
            is Estado.EnRevision -> "EnRevision"
            is Estado.Derivado -> "Derivado"
            is Estado.EnSeguimiento -> "EnSeguimiento"
        }
    }

    private fun stringToEstado(str: String): Estado {
        return when (str) {
            "EnRevision" -> Estado.EnRevision
            "Derivado" -> Estado.Derivado
            "EnSeguimiento" -> Estado.EnSeguimiento
            else -> Estado.Registrado
        }
    }

    private fun stringToCategoria(str: String): CategoriaCaso {
        return when (str) {
            "EspacioPublico" -> CategoriaCaso.EspacioPublico
            "ConvivenciaVecinal" -> CategoriaCaso.ConvivenciaVecinal
            "OtraSituacion" -> CategoriaCaso.OtraSituacion
            else -> CategoriaCaso.AlumbradoPublico
        }
    }

    private fun stringToPrioridad(str: String): Prioridad {
        return when (str) {
            "Alta" -> Prioridad.Alta
            "Baja" -> Prioridad.Baja
            else -> Prioridad.Media
        }
    }

    private fun stringToRol(str: String): UsuarioRol {
        return when (str) {
            "Operador" -> UsuarioRol.Operador
            "Supervisor" -> UsuarioRol.Supervisor
            "Administrador" -> UsuarioRol.Administrador
            else -> UsuarioRol.Residente
        }
    }

    private fun observacionesToJson(obsList: List<Observacion>): String {
        val jsonArray = JSONArray()
        for (obs in obsList) {
            val obj = JSONObject()
            obj.put("id", obs.id)
            obj.put("nota", obs.nota)
            obj.put("fechaHora", obs.fechaHora)
            obj.put("autor", obs.autor.javaClass.simpleName)
            jsonArray.put(obj)
        }
        return jsonArray.toString()
    }

    private fun jsonToObservaciones(jsonStr: String): List<Observacion> {
        val list = mutableListOf<Observacion>()
        try {
            if (jsonStr.isBlank()) return emptyList()
            val jsonArray = JSONArray(jsonStr)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.getString("id")
                val nota = obj.getString("nota")
                val fechaHora = obj.getLong("fechaHora")
                val autorStr = obj.getString("autor")
                val autorRol = stringToRol(autorStr)
                list.add(Observacion(id, nota, fechaHora, autorRol))
            }
        } catch (e: Exception) {
            // ignore
        }
        return list
    }
}
