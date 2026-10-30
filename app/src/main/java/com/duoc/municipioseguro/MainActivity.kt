package com.duoc.municipioseguro

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.municipioseguro.Model.UsuarioRol
import com.duoc.municipioseguro.ui.screens.AdministracionScreen
import com.duoc.municipioseguro.ui.screens.AvisosScreen
import com.duoc.municipioseguro.ui.screens.CrearCuentaScreen
import com.duoc.municipioseguro.ui.screens.CrearReporteScreen
import com.duoc.municipioseguro.ui.screens.DetalleCasoScreen
import com.duoc.municipioseguro.ui.screens.HomeScreen
import com.duoc.municipioseguro.ui.screens.LoginScreen
import com.duoc.municipioseguro.ui.screens.OlvidoContrasenaScreen
import com.duoc.municipioseguro.ui.screens.ReportesScreen
import com.duoc.municipioseguro.ui.theme.MunicipioSeguroTheme
import com.duoc.municipioseguro.viewModel.CasoSeguridadViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MunicipioSeguroTheme {
                AppNavegacion()
            }
        }
    }
}

@Composable
fun AppNavegacion(
    viewModel: CasoSeguridadViewModel = viewModel()
) {
    val sesionActual by viewModel.sesionActual.collectAsState()
    val context = LocalContext.current
    var authScreen by remember { mutableStateOf("login") }

    if (sesionActual == null) {
        when (authScreen) {
            "login" -> {
                LoginScreen(
                    onLoginSuccess = { rol ->
                        viewModel.iniciarSesionSimulada(rol)
                        Toast.makeText(
                            context,
                            "Sesión iniciada como: ${rol.javaClass.simpleName}",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onLoginSubmit = { correo, contrasena, rolSimulado ->
                        viewModel.iniciarSesionConRoom(correo, contrasena) { exito, mensaje ->
                            if (exito) {
                                Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                            } else {
                                // Fallback para prototipo si se usa el selector de simulación
                                viewModel.iniciarSesionSimulada(rolSimulado)
                                Toast.makeText(
                                    context,
                                    "$mensaje. Ingresando en modo prototipo ($correo)",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    },
                    onCreateAccountClick = {
                        authScreen = "create_account"
                    },
                    onForgotPasswordClick = {
                        authScreen = "forgot_password"
                    }
                )
            }
            "forgot_password" -> {
                OlvidoContrasenaScreen(
                    onBackToLogin = {
                        authScreen = "login"
                    },
                    onSendRecoveryEmail = { email ->
                        Toast.makeText(
                            context,
                            "Instrucciones enviadas a $email",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            }
            "create_account" -> {
                CrearCuentaScreen(
                    onBackToLogin = {
                        authScreen = "login"
                    },
                    onAccountCreated = { correo, contrasena, rol ->
                        viewModel.registrarUsuarioConRoom(correo, contrasena, rol) { _, mensaje ->
                            Toast.makeText(
                                context,
                                mensaje,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        authScreen = "login"
                    }
                )
            }
        }
    } else {
        val listaCasos by viewModel.listaCasos.collectAsState()
        val rolUsuario = sesionActual?.rol

        if (rolUsuario is UsuarioRol.Residente) {
            var currentMainScreen by remember { mutableStateOf("home") }
            var selectedCasoId by remember { mutableStateOf<String?>(null) }

            when (currentMainScreen) {
                "home" -> HomeScreen(
                    onNavigateHome = { currentMainScreen = "home" },
                    onNavigateReportes = { currentMainScreen = "reports" },
                    onNavigateAvisos = { currentMainScreen = "avisos" },
                    onCreateReportClick = { currentMainScreen = "create_report" },
                    onLogoutClick = {
                        viewModel.cerrarSesion()
                    }
                )
                "reports" -> ReportesScreen(
                    casos = listaCasos,
                    onNavigateHome = { currentMainScreen = "home" },
                    onNavigateReportes = { currentMainScreen = "reports" },
                    onNavigateAvisos = { currentMainScreen = "avisos" },
                    onReporteClick = { caso ->
                        selectedCasoId = caso.id
                        currentMainScreen = "report_detail"
                    },
                    onLogoutClick = {
                        viewModel.cerrarSesion()
                    }
                )
                "avisos" -> AvisosScreen(
                    onNavigateHome = { currentMainScreen = "home" },
                    onNavigateReportes = { currentMainScreen = "reports" },
                    onNavigateAvisos = { currentMainScreen = "avisos" },
                    onLogoutClick = {
                        viewModel.cerrarSesion()
                    }
                )
                "create_report" -> CrearReporteScreen(
                    onBackClick = { currentMainScreen = "home" },
                    onSubmitReport = { categoria, ubicacion, descripcion, prioridad, urlEvidencia ->
                        val nuevoId = "SJ-2026-${(100..999).random()}"
                        viewModel.registrarCaso(
                            id = nuevoId,
                            categoria = categoria,
                            ubicacion = ubicacion,
                            descripcion = descripcion,
                            prioridad = prioridad,
                            urlEvidencia = urlEvidencia
                        )
                        Toast.makeText(
                            context,
                            "Reporte $nuevoId registrado exitosamente",
                            Toast.LENGTH_LONG
                        ).show()
                        currentMainScreen = "reports"
                    }
                )
                "report_detail" -> {
                    val casoSeleccionado = listaCasos.find { it.id == selectedCasoId }
                    if (casoSeleccionado != null) {
                        DetalleCasoScreen(
                            caso = casoSeleccionado,
                            sesionActual = sesionActual,
                            onBackClick = { currentMainScreen = "reports" },
                            onCambiarEstado = { nuevoEstado ->
                                viewModel.cambiarEstadoCaso(casoSeleccionado.id, nuevoEstado)
                            },
                            onAgregarObservacion = { notaTexto ->
                                val autorRol = sesionActual?.rol ?: UsuarioRol.Residente
                                viewModel.registrarObservacion(casoSeleccionado.id, notaTexto, autorRol)
                            }
                        )
                    }
                }
            }
        } else {
            // Operador, Supervisor o Administrador
            var selectedCasoId by remember { mutableStateOf<String?>(null) }
            var showingDetail by remember { mutableStateOf(false) }

            if (showingDetail && selectedCasoId != null) {
                val casoSeleccionado = listaCasos.find { it.id == selectedCasoId }
                if (casoSeleccionado != null) {
                    DetalleCasoScreen(
                        caso = casoSeleccionado,
                        sesionActual = sesionActual,
                        onBackClick = { showingDetail = false },
                        onCambiarEstado = { nuevoEstado ->
                            viewModel.cambiarEstadoCaso(casoSeleccionado.id, nuevoEstado)
                        },
                        onAgregarObservacion = { notaTexto ->
                            val autorRol = sesionActual?.rol ?: UsuarioRol.Operador
                            viewModel.registrarObservacion(casoSeleccionado.id, notaTexto, autorRol)
                        }
                    )
                }
            } else {
                AdministracionScreen(
                    casos = listaCasos,
                    onLogoutClick = {
                        viewModel.cerrarSesion()
                    },
                    onNuevoCasoClick = {
                        Toast.makeText(context, "Crear nuevo caso municipal", Toast.LENGTH_SHORT).show()
                    },
                    onGestionarCasoClick = { caso ->
                        selectedCasoId = caso.id
                        showingDetail = true
                    }
                )
            }
        }
    }
}
