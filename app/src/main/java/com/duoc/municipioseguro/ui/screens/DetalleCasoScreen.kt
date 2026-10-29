package com.duoc.municipioseguro.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.municipioseguro.Model.*
import com.duoc.municipioseguro.ui.theme.MunicipioSeguroTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleCasoScreen(
    caso: CasoSeguridad,
    sesionActual: SesionUsuario?,
    onBackClick: () -> Unit,
    onCambiarEstado: (nuevoEstado: Estado) -> Unit,
    onAgregarObservacion: (notaTexto: String) -> Unit
) {
    val context = LocalContext.current
    var nuevaNotaText by remember { mutableStateOf("") }

    val primaryBlue = Color(0xFF23559C)
    val textDark = Color(0xFF1E293B)
    val textSecondary = Color(0xFF64748B)

    val esGestionador = sesionActual?.rol in listOf(
        UsuarioRol.Operador,
        UsuarioRol.Supervisor,
        UsuarioRol.Administrador
    )

    // Formato de fecha
    val dateFormat = SimpleDateFormat("dd 'de' MMMM yyyy, HH:mm 'hrs'", Locale.getDefault())
    val fechaFormateada = dateFormat.format(Date(caso.fechaHora))

    // Título según categoría
    val tituloCategoria = when (caso.categoria) {
        is CategoriaCaso.AlumbradoPublico -> "Alumbrado público"
        is CategoriaCaso.EspacioPublico -> "Espacio público"
        is CategoriaCaso.ConvivenciaVecinal -> "Convivencia vecinal"
        is CategoriaCaso.OtraSituacion -> "Otra situación"
    }

    // Datos visuales del estado actual
    val (estadoTexto, estadoBg, estadoColor) = when (caso.estado) {
        is Estado.Registrado -> Triple("Registrado", Color(0xFFE8F0FE), Color(0xFF23559C))
        is Estado.EnRevision -> Triple("En revisión", Color(0xFFFEF3C7), Color(0xFFD97706))
        is Estado.Derivado -> Triple("Derivado a cuadrilla", Color(0xFFE0E7FF), Color(0xFF4338CA))
        is Estado.EnSeguimiento -> Triple("En seguimiento / Resuelto", Color(0xFFDCFCE7), Color(0xFF15803D))
    }

    Scaffold(
        containerColor = Color(0xFFF7F9FC),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = caso.id,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = textDark
                        )
                        Text(
                            text = tituloCategoria,
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
                .padding(20.dp)
        ) {
            // Tarjeta 1: Estado y resumen principal
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = estadoBg
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CheckCircle,
                                    contentDescription = null,
                                    tint = estadoColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = estadoTexto,
                                    color = estadoColor,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Badge de Prioridad
                        val (prioTexto, prioBg, prioColor) = when (caso.prioridad) {
                            is Prioridad.Alta -> Triple("Prioridad Alta", Color(0xFFFEF2F2), Color(0xFF991B1B))
                            is Prioridad.Media -> Triple("Prioridad Media", Color(0xFFFFFBEB), Color(0xFFB45309))
                            is Prioridad.Baja -> Triple("Prioridad Baja", Color(0xFFF0FDF4), Color(0xFF15803D))
                        }

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = prioBg
                        ) {
                            Text(
                                text = prioTexto,
                                color = prioColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Descripción del reporte",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = textDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = caso.descripcion,
                        fontSize = 15.sp,
                        color = textDark,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Detalles de Ubicación y Fecha
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Place,
                            contentDescription = null,
                            tint = primaryBlue,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = caso.ubicacion,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textDark
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = null,
                            tint = textSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = fechaFormateada,
                            fontSize = 13.sp,
                            color = textSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Mapa de Google Maps
            val posicionCaso = LatLng(caso.latitud, caso.longitud)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(posicionCaso, 15f)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState
                    ) {
                        Marker(
                            state = MarkerState(position = posicionCaso),
                            title = caso.id,
                            snippet = caso.ubicacion
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tarjeta 2: Cambio de Estado (Solo si es Operador/Supervisor/Admin o Muestra rápida)
            if (esGestionador) {
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.AdminPanelSettings,
                                contentDescription = null,
                                tint = primaryBlue,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Panel de Gestión (${sesionActual?.rol?.javaClass?.simpleName})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = textDark
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Actualizar estado del caso:",
                            fontSize = 13.sp,
                            color = textSecondary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Botones de cambio de estado
                        val estadosDisponibles = listOf(
                            "Registrado" to Estado.Registrado,
                            "En revisión" to Estado.EnRevision,
                            "Derivado" to Estado.Derivado,
                            "En seguimiento" to Estado.EnSeguimiento
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            estadosDisponibles.forEach { (nombre, est) ->
                                val selected = caso.estado == est
                                FilterChip(
                                    selected = selected,
                                    onClick = {
                                        onCambiarEstado(est)
                                        Toast.makeText(context, "Estado actualizado a $nombre", Toast.LENGTH_SHORT).show()
                                    },
                                    label = { Text(nombre, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = primaryBlue,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Tarjeta 3: Historial de Observaciones
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Comment,
                            contentDescription = null,
                            tint = primaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Historial y observaciones (${caso.observaciones.size})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = textDark
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (caso.observaciones.isEmpty()) {
                        Text(
                            text = "Aún no hay observaciones registradas en este caso.",
                            fontSize = 13.sp,
                            color = textSecondary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            caso.observaciones.forEach { obs ->
                                ObservacionItem(obs = obs)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Spacer(modifier = Modifier.height(16.dp))

                    // Agregar nueva observación
                    Text(
                        text = "Agregar nueva nota u observación:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = textDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = nuevaNotaText,
                            onValueChange = { nuevaNotaText = it },
                            placeholder = { Text("Escribe un comentario o avance...", fontSize = 13.sp) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = false,
                            maxLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = primaryBlue,
                                unfocusedBorderColor = Color(0xFFCBD5E1)
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                if (nuevaNotaText.isNotBlank()) {
                                    onAgregarObservacion(nuevaNotaText)
                                    nuevaNotaText = ""
                                    Toast.makeText(context, "Observación agregada", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Escribe un comentario primero", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .background(primaryBlue, RoundedCornerShape(12.dp))
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.Send,
                                contentDescription = "Enviar nota",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ObservacionItem(obs: Observacion) {
    val obsDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val obsFecha = obsDateFormat.format(Date(obs.fechaHora))

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FAFC),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Autor: ${obs.autor.javaClass.simpleName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color(0xFF23559C)
                )
                Text(
                    text = obsFecha,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = obs.nota,
                fontSize = 13.sp,
                color = Color(0xFF1E293B)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetalleCasoScreenPreview() {
    val casoEjemplo = CasoSeguridad(
        id = "SJ-2026-0148",
        categoria = CategoriaCaso.AlumbradoPublico,
        fechaHora = System.currentTimeMillis() - 86400000,
        ubicacion = "Avenida Las Industrias 1200, San Joaquín",
        descripcion = "Poste chocado con cables expuestos en la vía pública.",
        prioridad = Prioridad.Alta,
        estado = Estado.EnRevision,
        rolAsignado = UsuarioRol.Operador,
        observaciones = listOf(
            Observacion("1", "Derivado a cuadrilla de emergencia eléctrica", System.currentTimeMillis() - 40000000, UsuarioRol.Operador)
        )
    )

    MunicipioSeguroTheme {
        DetalleCasoScreen(
            caso = casoEjemplo,
            sesionActual = SesionUsuario("Prueba", UsuarioRol.Operador),
            onBackClick = {},
            onCambiarEstado = {},
            onAgregarObservacion = {}
        )
    }
}
