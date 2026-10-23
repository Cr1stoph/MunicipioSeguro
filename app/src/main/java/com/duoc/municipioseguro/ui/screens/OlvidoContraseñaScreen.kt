package com.duoc.municipioseguro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LockReset
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.municipioseguro.ui.theme.MunicipioSeguroTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OlvidoContrasenaScreen(
    onBackToLogin: () -> Unit,
    onSendRecoveryEmail: (String) -> Unit = {}
) {
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Email, 1: SMS, 2: Soporte
    var mensajeEnviado by remember { mutableStateOf(false) }

    val primaryBlue = Color(0xFF1D58BA)
    val lightBlueBg = Color(0xFFEFF6FF)
    val textDark = Color(0xFF0F172A)
    val textSecondary = Color(0xFF64748B)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8FAFC)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Botón Volver
            IconButton(
                onClick = onBackToLogin,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(lightBlueBg)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Volver",
                    tint = primaryBlue
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Icono de cabecera
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(lightBlueBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.LockReset,
                    contentDescription = "Recuperar Contraseña",
                    tint = primaryBlue,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Título y Subtítulo
            Text(
                text = "¿Olvidaste tu contraseña?",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = textDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Selecciona el método de recuperación para restablecer tu acceso a Municipio Seguro de San Joaquín.",
                fontSize = 14.sp,
                color = textSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Pestañas / Opciones de Recuperación
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0; mensajeEnviado = false },
                    label = { Text("Correo", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = primaryBlue,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1; mensajeEnviado = false },
                    label = { Text("SMS", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Outlined.Phone, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = primaryBlue,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2; mensajeEnviado = false },
                    label = { Text("Soporte", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Outlined.SupportAgent, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = primaryBlue,
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Contenido según la pestaña seleccionada
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    when (selectedTab) {
                        0 -> {
                            // Recuperación por Correo
                            Text(
                                text = "Recuperación por Correo Electrónico",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = textDark
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Te enviaremos un enlace seguro para crear una nueva contraseña.",
                                fontSize = 13.sp,
                                color = textSecondary
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = correo,
                                onValueChange = { correo = it },
                                label = { Text("Correo electrónico registrado", fontSize = 12.sp) },
                                placeholder = { Text("nombre@ejemplo.cl") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = primaryBlue,
                                    unfocusedBorderColor = Color(0xFFE2E8F0)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    if (correo.isNotBlank()) {
                                        mensajeEnviado = true
                                        onSendRecoveryEmail(correo)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Text("Enviar enlace de recuperación", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                        1 -> {
                            // Recuperación por SMS
                            Text(
                                text = "Recuperación por Mensaje de Texto (SMS)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = textDark
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Ingresa tu número de celular asociado a tu cuenta para recibir un código de verificación de 6 dígitos.",
                                fontSize = 13.sp,
                                color = textSecondary
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = telefono,
                                onValueChange = { telefono = it },
                                label = { Text("Número de celular (+56 9 ...)", fontSize = 12.sp) },
                                placeholder = { Text("+56912345678") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = primaryBlue,
                                    unfocusedBorderColor = Color(0xFFE2E8F0)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    if (telefono.isNotBlank()) {
                                        mensajeEnviado = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Text("Enviar código SMS", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                        2 -> {
                            // Contactar Soporte Municipal
                            Text(
                                text = "Atención y Soporte Municipal",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = textDark
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Si no tienes acceso a tu correo o teléfono registrado, comunícate con la mesa de ayuda de Seguridad Comunal.",
                                fontSize = 13.sp,
                                color = textSecondary
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Card(
                                colors = CardDefaults.cardColors(containerColor = lightBlueBg),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp)
                                ) {
                                    Text("Teléfono de emergencia / soporte:", fontSize = 12.sp, color = textSecondary)
                                    Text("+56 2 2810 8000", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Correo de asistencia:", fontSize = 12.sp, color = textSecondary)
                                    Text("seguridad@sanjoaquin.cl", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                                }
                            }
                        }
                    }

                    // Mensaje de éxito simulado
                    if (mensajeEnviado && selectedTab != 2) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Surface(
                            color = Color(0xFFDCFCE7),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (selectedTab == 0) "¡Enlace enviado! Revisa tu bandeja de entrada." else "¡Código enviado a tu número de celular!",
                                color = Color(0xFF166534),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Botón para volver al login
            OutlinedButton(
                onClick = onBackToLogin,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Volver al inicio de sesión",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryBlue
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OlvidoContrasenaScreenPreview() {
    MunicipioSeguroTheme {
        OlvidoContrasenaScreen(onBackToLogin = {})
    }
}
