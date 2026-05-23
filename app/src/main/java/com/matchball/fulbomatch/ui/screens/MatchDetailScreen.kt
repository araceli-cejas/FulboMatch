package com.matchball.fulbomatch.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matchball.fulbomatch.ui.theme.GreenPrimary
import com.matchball.fulbomatch.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDetailScreen(
    matchId: String,
    onBackClick: () -> Unit
) {
    val match = mockMatches.find { it.id == matchId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del partido", color = White) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .semantics { contentDescription = "Detalle del partido" }
        ) {
            if (match != null) {
                Text(match.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text("📅 ${match.date} - ${match.time}", fontSize = 16.sp)
                Text("📍 ${match.location}", fontSize = 16.sp)
                Text("👥 Jugadores: ${match.players}", fontSize = 16.sp)
                Text("⚡ Nivel: ${match.level}", fontSize = 16.sp)

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { /* TODO: solicitar unirse */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Text("Solicitar unirme", fontSize = 16.sp, color = White)
                }
            } else {
                Text("Partido no encontrado", fontSize = 16.sp)
            }
        }
    }
}