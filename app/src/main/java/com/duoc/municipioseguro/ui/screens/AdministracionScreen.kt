package com.duoc.municipioseguro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.municipioseguro.Model.CasoSeguridad
import com.duoc.municipioseguro.Model.Estado
import com.duoc.municipioseguro.Model.Prioridad
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdministracionScreen(
    casos: List<CasoSeguridad>,
    onLogoutClick: () -> Unit,
    onNuevoCasoClick: () -> Unit = {},
    onGestionarCasoClick: (CasoSeguridad) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var estadoFilter by remember { mutableStateOf("Todos") } // "Todos", "Recibido", "En gestión", "Resuelto"
    var prioridadFilter by remember { mutableStateOf("Todas") } // "Todas", "Alta", "Media", "Baja"

    var estadoMenuExpanded by remember { mutableStateOf(false) }
    var filtrosMenuExpanded by remember { mutableStateOf(false) }

    // Contadores dinámicos para la tarjeta de resumen
    val recibidoCount = casos.count { it.estado is Estado.Registrado }
    val enGestionCount = casos.count { it.estado is Estado.EnRevision || it.estado is Estado.EnSeguimiento }
    val resueltoCount = casos.count { it.estado is Estado.Derivado }

    // Filtrado de casos
    val casosFiltrados = casos.filter { caso ->
        val matchSearch = searchQuery.isBlank() ||
            caso.id.contains(searchQuery, ignoreCase = true) ||
            caso.ubicacion.contains(searchQuery, ignoreCase = true) ||
            caso.descripcion.contains(searchQuery, ignoreCase = true)

        val matchEstado = when (estadoFilter) {
            "Recibido" -> caso.estado is Estado.Registrado
            "En gestión" -> caso.estado is Estado.EnRevision || caso.estado is Estado.EnSeguimiento
            "Resuelto" -> caso.estado is Estado.Derivado
            else -> true
        }

        val matchPrioridad = when (prioridadFilter) {
            "Alta" -> caso.prioridad is Prioridad.Alta
            "Media" -> caso.prioridad is Prioridad.Media
            "Baja" -> caso.prioridad is Prioridad.Baja
            else -> true
        }

        matchSearch && matchEstado && matchPrioridad
    }

    Scaffold(
        containerColor = Color(0xFFF8FAFC)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header: Shield Icon + Text & Profile Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Security,
                        contentDescription = "Shield Icon",
                        tint = Color(0xFF23559C),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Administración municipal",
                        color = Color(0xFF23559C),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                }

                IconButton(
                    onClick = onLogoutClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Perfil / Cerrar Sesión",
                        tint = Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main Title & "+ Nuevo caso" Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Casos",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp,
                    color = Color(0xFF0F172A)
                )

                Button(
                    onClick = onNuevoCasoClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D4ED8)),
                    shape = RoundedCornerShape(24.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Nuevo caso",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Summary Card (3 Stats) - Dinámico
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = recibidoCount.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Recibido",
                            color = Color(0xFF64748B),
                            fontSize = 13.sp
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = enGestionCount.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "En gestión",
                            color = Color(0xFF64748B),
                            fontSize = 13.sp
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = resueltoCount.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Resuelto",
                            color = Color(0xFF64748B),
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Buscar por folio o situación",
                        color = Color(0xFF94A3B8),
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF94A3B8)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF1F5F9),
                    unfocusedContainerColor = Color(0xFFF1F5F9),
                    disabledContainerColor = Color(0xFFF1F5F9),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filters Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = (estadoFilter == "Todos" && prioridadFilter == "Todas" && searchQuery.isBlank()),
                    onClick = {
                        estadoFilter = "Todos"
                        prioridadFilter = "Todas"
                        searchQuery = ""
                    },
                    label = { Text("Todos · ${casos.size}") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFE0E7FF),
                        selectedLabelColor = Color(0xFF1D4ED8)
                    ),
                    shape = RoundedCornerShape(20.dp)
                )

                Box {
                    FilterChip(
                        selected = estadoFilter != "Todos",
                        onClick = { estadoMenuExpanded = true },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(if (estadoFilter == "Todos") "Estado" else estadoFilter)
                                Spacer(modifier = Modifier.width(2.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            labelColor = Color(0xFF334155),
                            selectedContainerColor = Color(0xFFE0E7FF),
                            selectedLabelColor = Color(0xFF1D4ED8)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )

                    DropdownMenu(
                        expanded = estadoMenuExpanded,
                        onDismissRequest = { estadoMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Todos los estados") },
                            onClick = {
                                estadoFilter = "Todos"
                                estadoMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Recibido") },
                            onClick = {
                                estadoFilter = "Recibido"
                                estadoMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("En gestión") },
                            onClick = {
                                estadoFilter = "En gestión"
                                estadoMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Resuelto") },
                            onClick = {
                                estadoFilter = "Resuelto"
                                estadoMenuExpanded = false
                            }
                        )
                    }
                }

                Box {
                    FilterChip(
                        selected = prioridadFilter != "Todas",
                        onClick = { filtrosMenuExpanded = true },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.Tune,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (prioridadFilter == "Todas") "Filtros" else "Prioridad: $prioridadFilter")
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            labelColor = Color(0xFF334155),
                            selectedContainerColor = Color(0xFFE0E7FF),
                            selectedLabelColor = Color(0xFF1D4ED8)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )

                    DropdownMenu(
                        expanded = filtrosMenuExpanded,
                        onDismissRequest = { filtrosMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Todas las prioridades") },
                            onClick = {
                                prioridadFilter = "Todas"
                                filtrosMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Alta") },
                            onClick = {
                                prioridadFilter = "Alta"
                                filtrosMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Media") },
                            onClick = {
                                prioridadFilter = "Media"
                                filtrosMenuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Baja") },
                            onClick = {
                                prioridadFilter = "Baja"
                                filtrosMenuExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Results Subheader
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${casosFiltrados.size} casos encontrados",
                    color = Color(0xFF64748B),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Más recientes primero",
                    color = Color(0xFF64748B),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Cases LazyColumn
            if (casosFiltrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No se encontraron casos con los filtros seleccionados.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(casosFiltrados) { caso ->
                        CasoAdminCard(
                            caso = caso,
                            onGestionarClick = { onGestionarCasoClick(caso) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Prototipo de demostración · Datos ficticios",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CasoAdminCard(
    caso: CasoSeguridad,
    onGestionarClick: () -> Unit
) {
    val (badgeText, badgeIcon, badgeBgColor, badgeTextColor, subEstadoText) = when (caso.estado) {
        is Estado.Registrado -> Tuple5("Recibido", Icons.Outlined.CheckCircle, Color(0xFFE0E7FF), Color(0xFF1D4ED8), "Pendiente de asignación")
        is Estado.EnRevision, is Estado.EnSeguimiento -> Tuple5("En gestión", Icons.Outlined.Schedule, Color(0xFFFEF3C7), Color(0xFFD97706), "En revisión por equipo")
        is Estado.Derivado -> Tuple5("Resuelto", Icons.Outlined.CheckCircle, Color(0xFFDCFCE7), Color(0xFF15803D), "Caso finalizado")
    }

    val dateFormat = SimpleDateFormat("d 'oct.' yyyy", Locale.getDefault())
    val fechaStr = dateFormat.format(Date(caso.fechaHora))

    val tituloCaso = when (caso.categoria.javaClass.simpleName) {
        "AlumbradoPublico" -> "Luminaria apagada"
        "EspacioPublico" -> "Vereda obstruida"
        "ConvivenciaVecinal" -> "Problema de convivencia"
        else -> "Situación reportada"
    }

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
            // Top Row: Folio ID & State Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = caso.id,
                    color = Color(0xFF64748B),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = badgeBgColor
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = badgeIcon,
                            contentDescription = null,
                            tint = badgeTextColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = badgeText,
                            color = badgeTextColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Body: Title & Location
            Text(
                text = tituloCaso,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = caso.ubicacion,
                color = Color(0xFF64748B),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Row: Date & Sub-status + "Gestionar >" Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = fechaStr,
                        color = Color(0xFF64748B),
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subEstadoText,
                        color = Color(0xFF334155),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onGestionarClick() }
                ) {
                    Text(
                        text = "Gestionar",
                        color = Color(0xFF1D4ED8),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Gestionar",
                        tint = Color(0xFF1D4ED8),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// Data class auxiliar de 5 elementos para los badges de los casos
private data class Tuple5<A, B, C, D, E>(
    val a: A,
    val b: B,
    val c: C,
    val d: D,
    val e: E
)
