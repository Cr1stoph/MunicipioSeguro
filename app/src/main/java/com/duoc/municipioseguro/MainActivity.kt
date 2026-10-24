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
import com.duoc.municipioseguro.ui.screens.HomeScreen
import com.duoc.municipioseguro.ui.screens.LoginScreen
import com.duoc.municipioseguro.ui.screens.OlvidoContrasenaScreen
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
                        Toast.makeText(context, "Navegar a Crear Cuenta", Toast.LENGTH_SHORT).show()
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
        }
    } else {
        // Mostrar HomeScreen en vez de la pantalla temporal
        HomeScreen(
            onLogoutClick = {
                viewModel.cerrarSesion()
            }
        )
    }
}
