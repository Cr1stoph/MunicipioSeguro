package com.duoc.municipioseguro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.outlined.AddComment
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.municipioseguro.ui.theme.MunicipioSeguroTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateHome: () -> Unit = {},
    onNavigateReportes: () -> Unit = {},
    onNavigateAvisos: () -> Unit = {},
    onCreateReportClick: () -> Unit = {},
    onLogoutClick: () -> Unit
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Inicio", "Mis reportes", "Avisos")
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.List, Icons.Filled.Notifications)
    val unselectedIcons = listOf(Icons.Outlined.Home, Icons.Filled.List, Icons.Filled.Notifications)

    Scaffold(
        containerColor = Color(0xFFF7F9FC), // Fondo claro como en la imagen
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                                contentDescription = item
                            )
                        },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
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
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Home,
                        contentDescription = "Home Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFE8F0FE), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Seguridad comunal",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Municipalidad de San Joaquín",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
                IconButton(
                    onClick = onLogoutClick,
                    modifier = Modifier
                        .background(Color(0xFFF0F0F0), RoundedCornerShape(24.dp))
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Cerrar sesión (Temporal)",
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Title
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(
                    text = "Cuidemos nuestro barrio",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp,
                    lineHeight = 36.sp,
                    color = Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Informa situaciones no urgentes y revisa su seguimiento municipal.",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Create Report Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.AddComment,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color(0xFFE8F0FE), RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "¿Qué ocurre en tu sector?",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Repórtalo en dos pasos simples.",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onCreateReportClick,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23559C))
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Crear un reporte")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Tus reportes Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tus reportes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF1E293B)
                    )
                    TextButton(onClick = { /* TODO */ }) {
                        Text(
                            text = "Ver todos",
                            color = Color(0xFF23559C),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Metrics Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // En seguimiento Card
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "2",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 32.sp
                                )
                                Icon(
                                    imageVector = Icons.Outlined.Schedule,
                                    contentDescription = null,
                                    tint = Color(0xFF23559C)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "En seguimiento",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Resuelto Card
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "1",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 32.sp
                                )
                                Icon(
                                    imageVector = Icons.Outlined.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF0F766E) // Verde
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Resuelto",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Recent Report Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Luminaria apagada",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "SJ-2026-0148 · 15 oct.",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFE8F0FE)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CheckCircle, // Aproximación
                                    contentDescription = null,
                                    tint = Color(0xFF23559C),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Recibido",
                                    color = Color(0xFF23559C),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Emergency Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)), // Fondo rojizo
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Warning,
                            contentDescription = null,
                            tint = Color(0xFF991B1B) // Rojo oscuro
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "¿Hay una emergencia?",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF991B1B),
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Si hay peligro inmediato, llama al 133\n(Carabineros). Este canal no atiende emergencias.",
                                color = Color(0xFF991B1B),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Info Sector Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Notifications, // Aproximación al altavoz
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0xFFE8F0FE), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Información para tu sector",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Revisa los avisos comunales y reportes comunitarios",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                lineHeight = 18.sp
                            )
                        }
                        // Placeholder para el ">"
                        Text(text = ">", color = Color.Gray, fontSize = 20.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = "Prototipo de demostración · Datos ficticios",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MunicipioSeguroTheme {
        HomeScreen(onLogoutClick = {})
    }
}