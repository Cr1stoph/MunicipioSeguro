package com.duoc.municipioseguro.remote

import com.duoc.municipioseguro.data.local.CasoEntity
import com.duoc.municipioseguro.data.local.UsuarioEntity
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("caso")
    suspend fun obtenerCasos(): List<CasoEntity>

    @GET("caso/{id}")
    suspend fun obtenerCasoPorId(@Path("id") id: String): CasoEntity

    @POST("caso")
    suspend fun crearCaso(@Body caso: CasoEntity): CasoEntity

    @POST("caso/{id}")
    suspend fun actualizarCaso(@Path("id") id: String, @Body caso: CasoEntity): CasoEntity

    @DELETE("caso/{id}")
    suspend fun eliminarCaso(@Path("id") id: String)

    @GET("usuarios")
    suspend fun obtenerUsuarios(): List<UsuarioEntity>

    @POST("usuarios")
    suspend fun crearUsuario(@Body usuario: Map<String, String>): Any
}