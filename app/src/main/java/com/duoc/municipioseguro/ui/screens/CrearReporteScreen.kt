package com.duoc.municipioseguro.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.municipioseguro.Model.CategoriaCaso
import com.duoc.municipioseguro.Model.Prioridad
import com.duoc.municipioseguro.ui.theme.MunicipioSeguroTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearReporteScreen(
    onBackClick: () -> Unit,
    onSubmitReport: (categoria: CategoriaCaso, ubicacion: String, descripcion: String, prioridad: Prioridad) -> Unit
) {
    val context = LocalContext.current

    var categoriaSeleccionada by remember { mutableStateOf<CategoriaCaso>(CategoriaCaso.AlumbradoPublico) }
    var ubicacion by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var prioridadSeleccionada by remember { mutableStateOf<Prioridad>(Prioridad.Media) }
    var fotoAdjunta by remember { mutableStateOf(false) }

    val primaryBlue = Color(0xFF23559C)
    val lightBlueBg = Color(0xFFE8F0FE)
    val textDark = Color(0xFF1E293B)
    val textSecondary = Color(0xFF64748B)

    Scaffold(
        containerColor = Color(0xFFF7F9FC),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Crear nuevo reporte",
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
                .padding(20.dp)
        ) {
            // Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(lightBlueBg, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AddComment,
                            contentDescription = null,
                            tint = primaryBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Informa lo que ocurre en tu sector",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = textDark
                        )
                        Text(
                            text = "Tu reporte será recibido por el equipo de seguridad municipal.",
                            fontSize = 13.sp,
                            color = textSecondary,
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sección 1: Categoría
            Text(
                text = "1. Selecciona la categoría",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = textDark
            )
            Spacer(modifier = Modifier.height(10.dp))

            val categoriasList = listOf(
                CategoriaItem("Alumbrado público", "Postes apagados, cables expuestos", Icons.Outlined.Lightbulb, CategoriaCaso.AlumbradoPublico),
                CategoriaItem("Espacio público", "Veredas dañadas, escombros, plazas", Icons.Outlined.Park, CategoriaCaso.EspacioPublico),
                CategoriaItem("Convivencia vecinal", "Ruidos molestos, ruidos en plazas", Icons.Outlined.People, CategoriaCaso.ConvivenciaVecinal),
                CategoriaItem("Otra situación", "Otras situaciones no urgentes", Icons.AutoMirrored.Outlined.Help, CategoriaCaso.OtraSituacion)
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                categoriasList.forEach { item ->
                    val selected = categoriaSeleccionada == item.categoria
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { categoriaSeleccionada = item.categoria }
                            .then(
                                if (selected) Modifier.border(2.dp, primaryBlue, RoundedCornerShape(12.dp))
                                else Modifier
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selected) lightBlueBg.copy(alpha = 0.5f) else Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 2.dp else 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selected,
                                onClick = { categoriaSeleccionada = item.categoria },
                                colors = RadioButtonDefaults.colors(selectedColor = primaryBlue)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = if (selected) primaryBlue else textSecondary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = item.titulo,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = textDark
                                )
                                Text(
                                    text = item.subtitulo,
                                    fontSize = 12.sp,
                                    color = textSecondary
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sección 2: Ubicación
            Text(
                text = "2. Ubicación del incidente",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = textDark
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = ubicacion,
                onValueChange = { ubicacion = it },
                label = { Text("Ubicación o calle referencial") },
                placeholder = { Text("Ej: Av. Las Industrias con Sierra Bella") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Place,
                        contentDescription = null,
                        tint = textSecondary
                    )
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryBlue,
                    unfocusedBorderColor = Color(0xFFCBD5E1)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sección 3: Descripción
            Text(
                text = "3. Descripción de los hechos",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = textDark
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Detalla lo que está ocurriendo") },
                placeholder = { Text("Escribe brevemente los detalles, horarios o referencias útiles...") },
                minLines = 3,
                maxLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryBlue,
                    unfocusedBorderColor = Color(0xFFCBD5E1)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sección 4: Prioridad
            Text(
                text = "4. Nivel de urgencia percibido",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = textDark
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val prioridades = listOf(
                    "Baja" to Prioridad.Baja,
                    "Media" to Prioridad.Media,
                    "Alta" to Prioridad.Alta
                )

                prioridades.forEach { (nombre, prio) ->
                    val selected = prioridadSeleccionada == prio
                    FilterChip(
                        selected = selected,
                        onClick = { prioridadSeleccionada = prio },
                        label = { Text(nombre, fontWeight = FontWeight.SemiBold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = primaryBlue,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sección 5: Adjuntar Foto / Evidencia (Simulado)
            Text(
                text = "5. Evidencia fotográfica (Opcional)",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = textDark
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        fotoAdjunta = !fotoAdjunta
                        Toast
                            .makeText(
                                context,
                                if (fotoAdjunta) "Fotografía adjunta correctamente" else "Fotografía removida",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (fotoAdjunta) primaryBlue else Color(0xFFCBD5E1)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (fotoAdjunta) Icons.Outlined.CheckCircle else Icons.Outlined.CameraAlt,
                        contentDescription = "Adjuntar foto",
                        tint = if (fotoAdjunta) Color(0xFF15803D) else primaryBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (fotoAdjunta) "Foto adjunta (evidencia.jpg)" else "Tomar foto o subir desde la galería",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (fotoAdjunta) Color(0xFF15803D) else primaryBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tarjeta Informativa de Emergencia
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Warning,
                        contentDescription = null,
                        tint = Color(0xFF991B1B)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Si estás presenciando un delito o emergencia en progreso, llama al 133 (Carabineros) o al 1414 (Seguridad Municipal).",
                        fontSize = 12.sp,
                        color = Color(0xFF991B1B),
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Botón de Enviar
            Button(
                onClick = {
                    if (ubicacion.isBlank() || descripcion.isBlank()) {
                        Toast.makeText(context, "Por favor completa la ubicación y descripción", Toast.LENGTH_SHORT).show()
                    } else {
                        onSubmitReport(categoriaSeleccionada, ubicacion, descripcion, prioridadSeleccionada)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
            ) {
                Icon(imageVector = Icons.AutoMirrored.Outlined.Send, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Enviar reporte a la municipalidad",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private data class CategoriaItem(
    val titulo: String,
    val subtitulo: String,
    val icon: ImageVector,
    val categoria: CategoriaCaso
)

@Preview(showBackground = true)
@Composable
fun CrearReporteScreenPreview() {
    MunicipioSeguroTheme {
        CrearReporteScreen(
            onBackClick = {},
            onSubmitReport = { _, _, _, _ -> }
        )
    }
}
