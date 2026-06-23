package com.matchball.fulbomatch.ui.screens

import androidx.compose.foundation.background
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
import com.matchball.fulbomatch.ui.partido.PartidoUiState
import com.matchball.fulbomatch.ui.partido.PartidoViewModel
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.TextButton
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearPartidoScreen(
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onMatchesClick: () -> Unit,
    onProfileClick: () -> Unit,
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

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

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
        },
        bottomBar = {
            CrearPartidoBottomBar(
                isDarkMode = isDarkMode,
                onHomeClick = onHomeClick,
                onMatchesClick = onMatchesClick,
                onProfileClick = onProfileClick
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
                onValueChange = { }, // No se edita tipeando
                label = { Text("Fecha") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }, // Abre el diálogo
                enabled = false, // Lo deshabilitamos para que no salga el teclado
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = hora,
                onValueChange = { }, // No se edita tipeando
                label = { Text("Hora") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showTimePicker = true }, // Abre el diálogo
                enabled = false, // Lo deshabilitamos para que no salga el teclado
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
    // Diálogo del DatePicker
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        // Convertimos los milisegundos a formato DD/MM/AAAA
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        fecha = formatter.format(Date(millis))
                    }
                    showDatePicker = false
                    errorLocal = null
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Diálogo del TimePicker
    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    // Formateamos para que siempre tenga 2 dígitos (ej: 09:05)
                    val h = timePickerState.hour.toString().padStart(2, '0')
                    val m = timePickerState.minute.toString().padStart(2, '0')
                    hora = "$h:$m"
                    showTimePicker = false
                    errorLocal = null
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancelar")
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}

@Composable
private fun CrearPartidoBottomBar(
    isDarkMode: Boolean,
    onHomeClick: () -> Unit,
    onMatchesClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val background = if (isDarkMode) Color(0xFF151515) else Color(0xFFF3F1F1)
    val iconColor = if (isDarkMode) Color(0xFFC9D1C9) else Color(0xFF4F574C)
    val selectedBg = if (isDarkMode) Color(0xFF9EF49B) else Color(0xFF005C1F)
    val selectedIcon = if (isDarkMode) Color(0xFF111111) else Color(0xFFBDE8B9)

    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp),
        shape = RoundedCornerShape(18.dp),
        color = background,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(84.dp).padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Inicio
            Column(modifier = Modifier.width(64.dp).height(64.dp).clickable { onHomeClick() }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Icon(Icons.Default.Home, contentDescription = "Inicio", tint = iconColor, modifier = Modifier.size(26.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text("Inicio", color = iconColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            // Partidos
            Column(modifier = Modifier.width(64.dp).height(64.dp).clickable { onMatchesClick() }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text("⚽", fontSize = 24.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Partidos", color = iconColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            // Crear (Marcado como seleccionado)
            Box(modifier = Modifier.width(76.dp).height(66.dp).clip(RoundedCornerShape(22.dp)).background(selectedBg), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Icon(Icons.Default.Add, contentDescription = "Crear", tint = selectedIcon, modifier = Modifier.size(26.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Crear", color = selectedIcon, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
            // Perfil
            Column(modifier = Modifier.width(64.dp).height(64.dp).clickable { onProfileClick() }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Icon(Icons.Default.Person, contentDescription = "Perfil", tint = iconColor, modifier = Modifier.size(26.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text("Perfil", color = iconColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}