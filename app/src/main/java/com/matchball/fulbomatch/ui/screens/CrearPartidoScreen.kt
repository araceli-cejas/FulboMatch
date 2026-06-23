package com.matchball.fulbomatch.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.matchball.fulbomatch.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearPartidoScreen(
    onBackClick: () -> Unit,
    onPartidoCreado: () -> Unit,
    viewModel: PartidoViewModel = viewModel(),
    isDarkMode: Boolean
) {
    var titulo by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var lugar by remember { mutableStateOf("") }
    var maxJugadores by remember { mutableStateOf("10") }

    var errorLocal by remember { mutableStateOf<String?>(null) }

    val uiState by viewModel.uiState.collectAsState()

    // Si se crea con éxito, limpiamos el estado y navegamos atrás o al home
    LaunchedEffect(uiState) {
        if (uiState is PartidoUiState.Success) {
            viewModel.resetState()
            onPartidoCreado()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Organizar Partido") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it; errorLocal = null },
                label = { Text("Título (Ej: Fulbo 5 en Palermo)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it; errorLocal = null },
                label = { Text("Fecha (DD/MM/AAAA)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = hora,
                onValueChange = { hora = it; errorLocal = null },
                label = { Text("Hora (Ej: 20:00)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = lugar,
                onValueChange = { lugar = it; errorLocal = null },
                label = { Text("Lugar o Cancha") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = maxJugadores,
                onValueChange = { maxJugadores = it; errorLocal = null },
                label = { Text("Cantidad de Jugadores (Total)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mostrar errores
            if (errorLocal != null) {
                Text(errorLocal!!, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            } else if (uiState is PartidoUiState.Error) {
                Text((uiState as PartidoUiState.Error).message, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (titulo.isBlank() || fecha.isBlank() || hora.isBlank() || lugar.isBlank() || maxJugadores.isBlank()) {
                        errorLocal = "Por favor, completá todos los campos"
                    } else {
                        viewModel.crearPartido(
                            titulo = titulo,
                            fecha = fecha,
                            hora = hora,
                            lugar = lugar,
                            maxJugadores = maxJugadores.toIntOrNull() ?: 10
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled = uiState !is PartidoUiState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(26.dp)
            ) {
                if (uiState is PartidoUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Crear Partido", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}