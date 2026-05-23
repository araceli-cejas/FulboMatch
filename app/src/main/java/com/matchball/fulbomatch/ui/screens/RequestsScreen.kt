package com.matchball.fulbomatch.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
import com.matchball.fulbomatch.ui.theme.ErrorRed
import com.matchball.fulbomatch.ui.theme.GrayMedium
import com.matchball.fulbomatch.ui.theme.White

data class MockRequest(
    val id: String,
    val playerName: String,
    val matchTitle: String,
    val level: String,
    val status: String
)

val mockRequests = listOf(
    MockRequest("1", "Carlos Pérez", "Fulbito 5 en Palermo", "Intermedio", "pending"),
    MockRequest("2", "Martín López", "Fulbito 5 en Palermo", "Avanzado", "pending"),
    MockRequest("3", "Diego García", "Fútbol 7 en Núñez", "Principiante", "pending"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestsScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Solicitudes", color = White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenPrimary)
            )
        }
    ) { padding ->
        if (mockRequests.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No tenés solicitudes pendientes", color = GrayMedium)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .semantics { contentDescription = "Lista de solicitudes" },
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(mockRequests) { request ->
                    RequestCard(request)
                }
            }
        }
    }
}

@Composable
private fun RequestCard(request: MockRequest) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(request.playerName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("Partido: ${request.matchTitle}", fontSize = 14.sp, color = GrayMedium)
            Text("Nivel: ${request.level}", fontSize = 14.sp, color = GrayMedium)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* TODO: rechazar */ }) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Rechazar solicitud",
                        tint = ErrorRed
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { /* TODO: aceptar */ }) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Aceptar solicitud",
                        tint = GreenPrimary
                    )
                }
            }
        }
    }
}