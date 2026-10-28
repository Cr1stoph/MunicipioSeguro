package com.duoc.municipioseguro

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.municipioseguro.Model.UsuarioRol
import com.duoc.municipioseguro.ui.screens.AdministracionScreen
import com.duoc.municipioseguro.ui.screens.CrearCuentaScreen
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
                    onAccountCreated = { email ->
                        Toast.makeText(
                            context,
                            "Cuenta creada para $email. Inicia sesión.",
                            Toast.LENGTH_LONG
                        ).show()
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

            when (currentMainScreen) {
                "home" -> HomeScreen(
                    onNavigateHome = { currentMainScreen = "home" },
                    onNavigateReportes = { currentMainScreen = "reports" },
                    onNavigateAvisos = {
                        Toast.makeText(context, "Sección Avisos (Próximamente)", Toast.LENGTH_SHORT).show()
                    },
                    onLogoutClick = {
                        viewModel.cerrarSesion()
                    }
                )
                "reports" -> ReportesScreen(
                    casos = listaCasos,
                    onNavigateHome = { currentMainScreen = "home" },
                    onNavigateReportes = { currentMainScreen = "reports" },
                    onNavigateAvisos = {
                        Toast.makeText(context, "Sección Avisos (Próximamente)", Toast.LENGTH_SHORT).show()
                    },
                    onLogoutClick = {
                        viewModel.cerrarSesion()
                    }
                )
            }
        } else {
            // Operador, Supervisor o Administrador
            AdministracionScreen(
                casos = listaCasos,
                onLogoutClick = {
                    viewModel.cerrarSesion()
                },
                onNuevoCasoClick = {
                    Toast.makeText(context, "Crear nuevo caso municipal", Toast.LENGTH_SHORT).show()
                },
                onGestionarCasoClick = { caso ->
                    Toast.makeText(context, "Gestionar caso: ${caso.id}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
