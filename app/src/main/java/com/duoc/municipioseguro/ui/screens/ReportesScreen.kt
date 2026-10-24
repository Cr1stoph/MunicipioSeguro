package com.duoc.municipioseguro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Warning
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
import com.duoc.municipioseguro.ui.theme.MunicipioSeguroTheme
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReportesScreen(
    casos: List<CasoSeguridad>,
    onNavigateHome: () -> Unit,
    onNavigateReportes: () -> Unit,
    onNavigateAvisos: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Mis reportes, 1: Comunidad
    var selectedFilter by remember { mutableIntStateOf(0) } // 0: Todos, 1: En seguimiento, 2: Resueltos
    var bottomNavIndex by remember { mutableIntStateOf(1) } // "Mis reportes" seleccionado (índice 1)

    val items = listOf("Inicio", "Mis reportes", "Avisos")
    val selectedIcons = listOf(Icons.Filled.Home, Icons.AutoMirrored.Filled.List, Icons.Filled.Notifications)
    val unselectedIcons = listOf(Icons.Outlined.Home, Icons.AutoMirrored.Outlined.List, Icons.Outlined.Notifications)

    Scaffold(
        containerColor = Color(0xFFF7F9FC),
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                if (bottomNavIndex == index) selectedIcons[index] else unselectedIcons[index],
                                contentDescription = item
                            )
                        },
                        label = { Text(item) },
                        selected = bottomNavIndex == index,
                        onClick = {
                            bottomNavIndex = index
                            when (index) {
                                0 -> onNavigateHome()
                                1 -> onNavigateReportes()
                                2 -> onNavigateAvisos()
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Top Header & Logout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Reportes de la\ncomunidad",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 32.sp,
                        lineHeight = 36.sp,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Consulta pública de reportes",
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                }
                IconButton(
                    onClick = onLogoutClick,
                    modifier = Modifier
                        .background(Color(0xFFE8F0FE), RoundedCornerShape(24.dp))
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Cerrar sesión",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Toggle / Selector (Mis reportes / Comunidad)
            Row(
                modifier = Modifier
                    .background(Color(0xFFE2E8F0), RoundedCornerShape(24.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Button(
                    onClick = { selectedTab = 0 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == 0) Color.White else Color.Transparent,
                        contentColor = if (selectedTab == 0) Color(0xFF1E293B) else Color.Gray
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = if (selectedTab == 0) ButtonDefaults.buttonElevation(2.dp) else ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text("Mis reportes", fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = { selectedTab = 1 },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTab == 1) Color.White else Color.Transparent,
                        contentColor = if (selectedTab == 1) Color(0xFF1E293B) else Color.Gray
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = if (selectedTab == 1) ButtonDefaults.buttonElevation(2.dp) else ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text("Comunidad", fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Consulta los reportes visibles de la comunidad, sus estados, ubicaciones y fechas.",
                color = Color.Gray,
                fontSize = 14.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == 0,
                    onClick = { selectedFilter = 0 },
                    label = { Text("Todos · ${casos.size}") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF23559C),
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = selectedFilter == 1,
                    onClick = { selectedFilter = 1 },
                    label = { Text("En seguimiento · 2") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF23559C),
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = selectedFilter == 2,
                    onClick = { selectedFilter = 2 },
                    label = { Text("Resueltos · 1") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF23559C),
                        selectedLabelColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Results subheader
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${casos.size} reportes visibles",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Más recientes primero",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // LazyColumn of Reports
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(casos) { caso ->
                    ReporteCard(caso = caso)
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun ReporteCard(caso: CasoSeguridad) {
    // Definir color y texto del badge según estado
    val (badgeText, badgeBgColor, badgeTextColor) = when (caso.estado) {
        is Estado.Registrado -> Triple("Recibido", Color(0xFFE8F0FE), Color(0xFF23559C))
        is Estado.EnRevision -> Triple("En gestión", Color(0xFFFEF3C7), Color(0xFFD97706))
        is Estado.EnSeguimiento -> Triple("Resuelto", Color(0xFFDCFCE7), Color(0xFF15803D))
        else -> Triple("Recibido", Color(0xFFE8F0FE), Color(0xFF23559C))
    }

    // Formatear fecha aproximada o usar la fecha del objeto
    val dateFormat = SimpleDateFormat("d 'oct.' yyyy", Locale("es", "ES"))
    val fechaStr = dateFormat.format(Date(caso.fechaHora))

    // Título según categoría
    val tituloCategoria = when (caso.categoria.javaClass.simpleName) {
        "AlumbradoPublico" -> "Luminaria apagada"
        "EspacioPublico" -> "Vereda obstruida"
        else -> caso.categoria.toString()
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
            // First row: Badge & Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = badgeBgColor
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
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
                Text(
                    text = fechaStr,
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Second row: Icon, Title & Chevron
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Lightbulb,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFE8F0FE), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = tituloCategoria,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color(0xFF1E293B)
                    )
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Detalle",
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Location
            Text(
                text = caso.ubicacion,
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom row: ID & Status message
            Column {
                Text(
                    text = caso.id,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = caso.descripcion,
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }
        }
    }
}
