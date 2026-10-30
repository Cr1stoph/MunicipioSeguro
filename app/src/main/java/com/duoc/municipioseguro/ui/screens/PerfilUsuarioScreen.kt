package com.duoc.municipioseguro.ui.screens

import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.municipioseguro.Model.SesionUsuario
import com.duoc.municipioseguro.Model.UsuarioRol
import com.duoc.municipioseguro.ui.theme.MunicipioSeguroTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilUsuarioScreen(
    sesionActual: SesionUsuario?,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val context = LocalContext.current

    val primaryBlue = Color(0xFF23559C)
    val lightBlueBg = Color(0xFFE8F0FE)
    val textDark = Color(0xFF1E293B)
    val textSecondary = Color(0xFF64748B)

    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Lanzador de la cámara para cambiar foto de perfil
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bmp ->
        if (bmp != null) {
            profileBitmap = bmp
            Toast.makeText(context, "Foto de perfil actualizada", Toast.LENGTH_SHORT).show()
        }
    }

    val nombreUsuario = sesionActual?.nombreGenerico ?: "Vecino de San Joaquín"
    val rolNombre = sesionActual?.rol?.javaClass?.simpleName ?: "Residente"

    Scaffold(
        containerColor = Color(0xFFF7F9FC),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Mi Perfil",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = textDark
                        )
                        Text(
                            text = "Municipalidad de San Joaquín",
                            fontSize = 12.sp,
                            color = textSecondary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = textDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Avatar & Header Card con opción de Cámara
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .clip(CircleShape)
                            .background(lightBlueBg)
                            .clickable {
                                cameraLauncher.launch(null)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (profileBitmap != null) {
                            Image(
                                bitmap = profileBitmap!!.asImageBitmap(),
                                contentDescription = "Foto de perfil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = "Avatar",
                                tint = primaryBlue,
                                modifier = Modifier.size(44.dp)
                            )
                        }

                        // Badge de Cámara sobrepuesto
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(28.dp)
                                .background(primaryBlue, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CameraAlt,
                                contentDescription = "Cambiar foto",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(onClick = { cameraLauncher.launch(null) }) {
                        Text(
                            text = if (profileBitmap != null) "Cambiar foto de perfil" else "Tomar foto de perfil",
                            fontSize = 13.sp,
                            color = primaryBlue,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = nombreUsuario,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = textDark
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = lightBlueBg
                    ) {
                        Text(
                            text = "Rol: $rolNombre",
                            color = primaryBlue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tarjeta: Información de la cuenta
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Datos de la cuenta",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = textDark
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PerfilItemInfo(
                        icon = Icons.Outlined.Email,
                        label = "Correo electrónico",
                        valor = "usuario@sanjoaquin.cl"
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(12.dp))

                    PerfilItemInfo(
                        icon = Icons.Outlined.Phone,
                        label = "Teléfono de contacto",
                        valor = "+56 9 8765 4321"
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(12.dp))

                    PerfilItemInfo(
                        icon = Icons.Outlined.LocationOn,
                        label = "Comuna y Sector",
                        valor = "San Joaquín · Sector Centro"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tarjeta: Opciones y Ajustes
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    PerfilOpcionBoton(
                        icon = Icons.Outlined.Notifications,
                        titulo = "Notificaciones de reportes",
                        onClick = {
                            Toast.makeText(context, "Notificaciones activas", Toast.LENGTH_SHORT).show()
                        }
                    )
                    PerfilOpcionBoton(
                        icon = Icons.Outlined.Lock,
                        titulo = "Seguridad y contraseña",
                        onClick = {
                            Toast.makeText(context, "Ajustes de contraseña", Toast.LENGTH_SHORT).show()
                        }
                    )
                    PerfilOpcionBoton(
                        icon = Icons.Outlined.Shield,
                        titulo = "Términos y privacidad",
                        onClick = {
                            Toast.makeText(context, "Municipalidad de San Joaquín 2026", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Cerrar Sesión
            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEF2F2))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                    contentDescription = "Cerrar sesión",
                    tint = Color(0xFF991B1B)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cerrar sesión",
                    color = Color(0xFF991B1B),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Prototipo de demostración · San Joaquín",
                fontSize = 11.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PerfilItemInfo(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    valor: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF23559C),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color(0xFF64748B)
            )
            Text(
                text = valor,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E293B)
            )
        }
    }
}

@Composable
fun PerfilOpcionBoton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    titulo: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF1E293B),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = titulo,
                color = Color(0xFF1E293B),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PerfilUsuarioScreenPreview() {
    MunicipioSeguroTheme {
        PerfilUsuarioScreen(
            sesionActual = SesionUsuario("Vecino Prueba", UsuarioRol.Residente),
            onBackClick = {},
            onLogoutClick = {}
        )
    }
}
