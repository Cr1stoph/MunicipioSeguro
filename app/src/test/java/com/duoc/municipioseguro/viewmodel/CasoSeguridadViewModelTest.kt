package com.duoc.municipioseguro.viewModel

import android.app.Application
import android.content.SharedPreferences
import com.duoc.municipioseguro.Model.CasoSeguridad
import com.duoc.municipioseguro.Model.CategoriaCaso
import com.duoc.municipioseguro.Model.Estado
import com.duoc.municipioseguro.Model.Prioridad
import com.duoc.municipioseguro.repository.AuthRepository
import com.duoc.municipioseguro.repository.CasoSeguridadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class CasoSeguridadViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    @Test
    fun `publica los casos recibidos al iniciar`() = runTest {
        val casosPrueba = listOf(
            CasoSeguridad(
                id = "SJ-2026-0148",
                categoria = CategoriaCaso.AlumbradoPublico,
                fechaHora = 1792022400000L,
                ubicacion = "Av. Departamental",
                descripcion = "Luminaria apagada",
                prioridad = Prioridad.Media,
                estado = Estado.Registrado,
                rolAsignado = null
            )
        )

        val mockApp = mock(Application::class.java)
        val mockPrefs = mock(SharedPreferences::class.java)
        `when`(mockApp.applicationContext).thenReturn(mockApp)
        `when`(mockApp.getSharedPreferences(anyString(), anyInt())).thenReturn(mockPrefs)

        val repositorioFalso = object : CasoSeguridadRepository(mockApp) {
            override suspend fun obtenerTodosLosCasos(): List<CasoSeguridad> = casosPrueba
        }
        val authFalso = object : AuthRepository(mockApp) {
            override fun obtenerSesionActual() = null
        }

        val viewModel = CasoSeguridadViewModel(
            application = mockApp,
            casoRepository = repositorioFalso,
            authRepository = authFalso
        )

        assertEquals(emptyList<CasoSeguridad>(), viewModel.listaCasos.value)

        advanceUntilIdle()

        assertEquals(casosPrueba, viewModel.listaCasos.value)
    }
}
