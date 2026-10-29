package com.duoc.municipioseguro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.municipioseguro.ui.theme.MunicipioSeguroTheme

data class AvisoComunal(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val etiqueta: String,
    val fecha: String,
    val esUrgente: Boolean = false
)

@Composable
fun AvisosScreen(
    onNavigateHome: () -> Unit,
    onNavigateReportes: () -> Unit,
    onNavigateAvisos: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var selectedFilter by remember { mutableIntStateOf(0) } // 0: Todos, 1: Importantes, 2: Servicios
    var bottomNavIndex by remember { mutableIntStateOf(2) } // "Avisos" seleccionado (índice 2)

    val items = listOf("Inicio", "Mis reportes", "Avisos")
    val selectedIcons = listOf(Icons.Filled.Home, Icons.AutoMirrored.Filled.List, Icons.Filled.Notifications)
    val unselectedIcons = listOf(Icons.Outlined.Home, Icons.AutoMirrored.Outlined.List, Icons.Outlined.Notifications)

    val avisosEjemplo = remember {
        listOf(
            AvisoComunal(
                id = "AV-01",
                titulo = "Mantenimiento preventivo en alumbrado público",
                descripcion = "Se realizarán trabajos de mejora y cambio de luminarias LED en Av. Las Industrias entre Sierra Bella y Carlos Valdovinos. Posibles cortes temporales.",
                etiqueta = "Corte programado",
                fecha = "Hoy · 10:30 hrs",
                esUrgente = true
            ),
            AvisoComunal(
                id = "AV-02",
                titulo = "Operativo veterinario gratuito en Plaza Central",
                descripcion = "Vacunación antirrábica, desparasitación e implantación de microchip para perros y gatos. Atención por orden de llegada.",
                etiqueta = "Servicio comunitario",
                fecha = "Ayer · 16:00 hrs"
            ),
            AvisoComunal(
                id = "AV-03",
                titulo = "Capacitación en alarmas e iluminación comunitaria",
                descripcion = "Invitamos a todos los vecinos del sector a participar en el taller de prevención del delito organizado por la Dirección de Seguridad Comunal.",
                etiqueta = "Taller vecinal",
                fecha = "25 oct. 2026"
            )
        )
    }

    val avisosFiltrados = when (selectedFilter) {
        1 -> avisosEjemplo.filter { it.esUrgente }
        2 -> avisosEjemplo.filter { !it.esUrgente }
        else -> avisosEjemplo
    }

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
                        text = "Avisos comunales",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 32.sp,
                        lineHeight = 36.sp,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Municipalidad de San Joaquín",
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

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Infórmate sobre novedades, cortes programados y actividades para tu barrio.",
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
                    label = { Text("Todos · ${avisosEjemplo.size}") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF23559C),
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = selectedFilter == 1,
                    onClick = { selectedFilter = 1 },
                    label = { Text("Importantes · 1") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF23559C),
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = selectedFilter == 2,
                    onClick = { selectedFilter = 2 },
                    label = { Text("Servicios · 2") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF23559C),
                        selectedLabelColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // LazyColumn de Avisos
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(avisosFiltrados) { aviso ->
                    AvisoCard(aviso = aviso)
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Prototipo de demostración · Datos ficticios",
                        color = Color.Gray,
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

@Composable
fun AvisoCard(aviso: AvisoComunal) {
    val (badgeBgColor, badgeTextColor) = if (aviso.esUrgente) {
        Pair(Color(0xFFFEF3C7), Color(0xFFD97706)) // Naranja / Ámbar
    } else {
        Pair(Color(0xFFE8F0FE), Color(0xFF23559C)) // Azul
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
            // First Row: Tag & Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = badgeBgColor
                ) {
                    Text(
                        text = aviso.etiqueta,
                        color = badgeTextColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = aviso.fecha,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Second Row: Icon + Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Campaign,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFE8F0FE), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = aviso.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1E293B),
                    lineHeight = 20.sp,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = aviso.descripcion,
                color = Color(0xFF475569),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AvisosScreenPreview() {
    MunicipioSeguroTheme {
        AvisosScreen(
            onNavigateHome = {},
            onNavigateReportes = {},
            onNavigateAvisos = {},
            onLogoutClick = {}
        )
    }
}
