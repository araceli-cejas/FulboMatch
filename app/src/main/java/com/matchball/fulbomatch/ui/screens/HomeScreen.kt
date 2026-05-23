package com.matchball.fulbomatch.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matchball.fulbomatch.ui.theme.GreenPrimary
import com.matchball.fulbomatch.ui.theme.White

// Datos mockeados
data class MockMatch(
    val id: String,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val players: String,
    val level: String
)

val mockMatches = listOf(
    MockMatch("1", "Fulbito 5 en Palermo", "Sábado 14/06", "18:00", "Cancha Los Amigos", "7/10", "Intermedio"),
    MockMatch("2", "Partido 11 en Flores", "Domingo 15/06", "10:00", "Club El Sol", "15/22", "Principiante"),
    MockMatch("3", "Fútbol 7 en Núñez", "Lunes 16/06", "20:30", "Complejo Norte", "5/14", "Avanzado"),
    MockMatch("4", "Picadito en Caballito", "Martes 17/06", "19:00", "Plaza Rivadavia", "8/10", "Intermedio"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMatchClick: (String) -> Unit,
    onCreateMatchClick: () -> Unit,
    onProfileClick: () -> Unit,
    onRequestsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "fulbomatch",
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                },
                actions = {
                    IconButton(onClick = onRequestsClick) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Ver solicitudes",
                            tint = White
                        )
                    }
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Mi perfil",
                            tint = White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenPrimary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateMatchClick,
                containerColor = GreenPrimary,
                contentColor = White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear partido")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .semantics { contentDescription = "Pantalla principal con partidos disponibles" }
        ) {
            Text(
                text = "Partidos disponibles",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(mockMatches) { match ->
                    MatchCard(
                        match = match,
                        onClick = { onMatchClick(match.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MatchCard(match: MockMatch, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = match.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "📅 ${match.date} - ${match.time}", fontSize = 14.sp)
            Text(text = "📍 ${match.location}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "👥 ${match.players}", fontSize = 13.sp)
                Text(
                    text = match.level,
                    fontSize = 13.sp,
                    color = GreenPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}