package com.duoc.municipioseguro.repository

import com.duoc.municipioseguro.remote.ApiService
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CasoSeguridadRepositoryTest {
    private lateinit var servidor: MockWebServer
    private lateinit var api: ApiService

    @Before
    fun preparar() {
        servidor = MockWebServer()
        servidor.start()
        api = Retrofit.Builder()
            .baseUrl(servidor.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun limpiar() {
        servidor.shutdown()
    }

    @Test
    fun `obtenerCasos desde MockWebServer retorna lista correcta`() = runTest {
        val jsonMock = """
            [
                {
                    "id": "SJ-2026-0148",
                    "categoria": "AlumbradoPublico",
                    "fechaHora": 1792022400000,
                    "ubicacion": "Av. Departamental",
                    "descripcion": "Luminaria apagada",
                    "prioridad": "Media",
                    "estado": "Registrado",
                    "rolAsignado": null,
                    "urlEvidencia": null,
                    "latitud": -33.4883,
                    "longitud": -70.6233,
                    "autorEmail": "vecino@sanjoaquin.cl",
                    "observacionesJson": "[]"
                }
            ]
        """.trimIndent()

        servidor.enqueue(MockResponse().setBody(jsonMock).setResponseCode(200))

        val casos = api.obtenerCasos()

        assertEquals(1, casos.size)
        assertEquals("SJ-2026-0148", casos[0].id)
        assertEquals("AlumbradoPublico", casos[0].categoria)

        val solicitud = servidor.takeRequest()
        assertEquals("GET", solicitud.method)
        assertEquals("/caso", solicitud.path)
    }
}
