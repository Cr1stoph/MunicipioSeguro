package com.duoc.municipioseguro.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CasoDao {
    @Query("SELECT * FROM casos ORDER BY fechaHora DESC")
    suspend fun obtenerTodosLosCasos(): List<CasoEntity>

    @Query("SELECT * FROM casos WHERE id = :id LIMIT 1")
    suspend fun obtenerCasoPorId(id: String): CasoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCaso(caso: CasoEntity): Long

    @Query("UPDATE casos SET estado = :nuevoEstado WHERE id = :idCaso")
    suspend fun actualizarEstado(idCaso: String, nuevoEstado: String): Int

    @Query("UPDATE casos SET observacionesJson = :observacionesJson WHERE id = :idCaso")
    suspend fun actualizarObservaciones(idCaso: String, observacionesJson: String): Int

    @Query("SELECT COUNT(*) FROM casos")
    suspend fun contarCasos(): Int
}
