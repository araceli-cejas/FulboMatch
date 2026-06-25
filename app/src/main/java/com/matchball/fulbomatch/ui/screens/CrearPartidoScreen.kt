package com.matchball.fulbomatch.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import com.matchball.fulbomatch.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.matchball.fulbomatch.ui.components.MoonIconButton
import com.matchball.fulbomatch.ui.partido.PartidoUiState
import com.matchball.fulbomatch.ui.partido.PartidoViewModel
import com.matchball.fulbomatch.ui.theme.GreenPrimary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearPartidoScreen(
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onMatchesClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit = {},
    onPartidoCreado: () -> Unit,
    onToggleDarkMode: () -> Unit,
    viewModel: PartidoViewModel = viewModel(),
    isDarkMode: Boolean
) {
    var titulo by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var lugar by remember { mutableStateOf("") }
    var maxJugadores by remember { mutableStateOf("10") }

    val uiState by viewModel.uiState.collectAsState()
    val colors = crearPartidoColors(isDarkMode)

    // Estado para los Pickers
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    // Navegar cuando el partido se crea con éxito
    LaunchedEffect(uiState) {
        if (uiState is PartidoUiState.Success) {
            onPartidoCreado()
        }
    }

    // Colores dinámicos
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = colors.textPrimary,
        unfocusedTextColor = colors.textPrimary,
        focusedContainerColor = colors.inputBackground,
        unfocusedContainerColor = colors.inputBackground,
        focusedBorderColor = GreenPrimary,
        unfocusedBorderColor = colors.border,
        focusedLabelColor = GreenPrimary,
        unfocusedLabelColor = colors.textSecondary,
        disabledBorderColor = colors.border,
        disabledTextColor = colors.textPrimary,
        disabledLabelColor = colors.textSecondary,
        disabledContainerColor = colors.inputBackground,
        disabledPlaceholderColor = colors.textSecondary
    )

    Scaffold(
        containerColor = colors.background,
        bottomBar = { CrearPartidoBottomBar(colors, onHomeClick, onMatchesClick, onProfileClick) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(colors.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            CrearPartidoHeader(
                colors = colors,
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
                onNotificationsClick = onNotificationsClick
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Organizar Partido",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = titulo, onValueChange = { titulo = it },
                label = { Text("Título del partido") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )
            Spacer(modifier = Modifier.height(16.dp))

            // FECHA CON PICKER
            OutlinedTextField(
                value = fecha,
                onValueChange = { },
                label = { Text("Fecha") },
                modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
                placeholder = { Text("Seleccionar fecha") },
                readOnly = true,
                enabled = false,
                trailingIcon = { 
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Seleccionar fecha", tint = colors.headerIcon)
                    }
                },
                colors = textFieldColors
            )
            Spacer(modifier = Modifier.height(16.dp))

            // HORA CON PICKER
            OutlinedTextField(
                value = hora,
                onValueChange = { },
                label = { Text("Hora") },
                modifier = Modifier.fillMaxWidth().clickable { showTimePicker = true },
                placeholder = { Text("Seleccionar hora") },
                readOnly = true,
                enabled = false,
                trailingIcon = {
                    IconButton(onClick = { showTimePicker = true }) {
                        Icon(Icons.Default.AccessTime, contentDescription = "Seleccionar hora", tint = colors.headerIcon)
                    }
                },
                colors = textFieldColors
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = lugar, onValueChange = { lugar = it },
                label = { Text("Lugar") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = maxJugadores,
                onValueChange = { 
                    if (it.all { char -> char.isDigit() } && it.length <= 2) {
                        maxJugadores = it
                    }
                },
                label = { Text("Max Jugadores (Mín. 2)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { 
                    val playersCount = maxJugadores.toIntOrNull() ?: 10
                    if (playersCount >= 2) {
                        viewModel.crearPartido(titulo, fecha, hora, lugar, playersCount)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                enabled = fecha.isNotBlank() && hora.isNotBlank() && titulo.isNotBlank() && lugar.isNotBlank() && (maxJugadores.toIntOrNull() ?: 0) >= 2,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenPrimary,
                    disabledContainerColor = if (isDarkMode) Color(0xFF3A4A3A) else Color(0xFFB8C7B8)
                )
            ) {
                Text("Crear Partido", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }

    // DIALOGS
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
                        fecha = sdf.format(Date(it))
                    }
                    showDatePicker = false
                }) { Text("OK", color = GreenPrimary) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val h = if (timePickerState.hour < 10) "0${timePickerState.hour}" else "${timePickerState.hour}"
                    val m = if (timePickerState.minute < 10) "0${timePickerState.minute}" else "${timePickerState.minute}"
                    hora = "$h:$m"
                    showTimePicker = false
                }) { Text("OK", color = GreenPrimary) }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancelar") }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}

private fun isCompleteDate(value: String): Boolean {
    return value.length == 10
}

private fun isCompleteTime(value: String): Boolean {
    return value.length == 5
}

@Composable
private fun CrearPartidoHeader(
    colors: CrearPartidoColors,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    val logoRes = if (isDarkMode) {
        R.drawable.logo_dark
    } else {
        R.drawable.nombre
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = logoRes),
            contentDescription = "Logo FulboMatch",
            modifier = Modifier
                .width(190.dp)
                .height(42.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.weight(1f))

        MoonIconButton(
            isDarkMode = isDarkMode,
            iconColor = colors.headerIcon,
            onClick = onToggleDarkMode
        )

        IconButton(onClick = onNotificationsClick) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notificaciones",
                tint = colors.headerIcon
            )
        }
    }
}

@Composable
private fun CrearPartidoBottomBar(
    colors: CrearPartidoColors,
    onHomeClick: () -> Unit,
    onMatchesClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp),
        shape = RoundedCornerShape(18.dp),
        color = colors.bottomBarBackground,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp)
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            UnselectedBottomItem(
                icon = Icons.Default.Home,
                label = "Inicio",
                colors = colors,
                onClick = onHomeClick
            )

            UnselectedBottomItem(
                iconText = "⚽",
                label = "Partidos",
                colors = colors,
                onClick = onMatchesClick
            )

            SelectedBottomItem(
                icon = Icons.Default.Add,
                label = "Crear",
                onClick = { }
            )

            UnselectedBottomItem(
                icon = Icons.Default.Person,
                label = "Perfil",
                colors = colors,
                onClick = onProfileClick
            )
        }
    }
}

@Composable
private fun SelectedBottomItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(76.dp)
            .height(66.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(GreenPrimary)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFFBDE8B9),
                modifier = Modifier.size(26.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = label,
                color = Color(0xFFBDE8B9),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun UnselectedBottomItem(
    icon: ImageVector,
    label: String,
    colors: CrearPartidoColors,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(64.dp)
            .height(64.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = colors.bottomIcon,
            modifier = Modifier.size(26.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = colors.bottomIcon,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun UnselectedBottomItem(
    iconText: String,
    label: String,
    colors: CrearPartidoColors,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(64.dp)
            .height(64.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = iconText,
            fontSize = 24.sp,
            color = colors.bottomIcon
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = colors.bottomIcon,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

private data class CrearPartidoColors(
    val background: Color,
    val headerBackground: Color,
    val inputBackground: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val headerIcon: Color,
    val border: Color,
    val bottomBarBackground: Color,
    val bottomIcon: Color
)

private fun crearPartidoColors(isDarkMode: Boolean): CrearPartidoColors {
    return if (isDarkMode) {
        CrearPartidoColors(
            background = Color(0xFF111111),
            headerBackground = Color(0xFF111111),
            inputBackground = Color(0xFF1A1A1A),
            textPrimary = Color(0xFFEDEDED),
            textSecondary = Color(0xFFBDBDBD),
            headerIcon = Color(0xFFC9D1C9),
            border = Color(0xFF3E463E),
            bottomBarBackground = Color(0xFF151515),
            bottomIcon = Color(0xFFC9D1C9)
        )
    } else {
        CrearPartidoColors(
            background = Color(0xFFF6F6F6),
            headerBackground = Color.White,
            inputBackground = Color.White,
            textPrimary = Color.Black,
            textSecondary = Color(0xFF555555),
            headerIcon = GreenPrimary,
            border = Color(0xFFD0D0D0),
            bottomBarBackground = Color(0xFFF3F1F1),
            bottomIcon = Color(0xFF4F574C)
        )
    }
}
