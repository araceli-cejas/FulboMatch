package com.matchball.fulbomatch.ui.screens

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import com.matchball.fulbomatch.ui.partido.PartidoViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matchball.fulbomatch.ui.components.MoonIconButton
import com.matchball.fulbomatch.ui.theme.GreenPrimary
import com.matchball.fulbomatch.ui.theme.White
import com.matchball.fulbomatch.ui.partido.PartidoUiState

private data class EditMatchColors(
    val background: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val inputBackground: Color,
    val inputBorder: Color,
    val icon: Color,
    val headerIcon: Color,
    val accent: Color,
    val accentText: Color,
    val disabledButton: Color,
    val segmentedBackground: Color,
    val optionBackground: Color,
    val optionBorder: Color,
    val infoBoxBackground: Color,
    val infoBoxText: Color,
    val danger: Color,
    val bottomBarBackground: Color,
    val bottomIcon: Color,
    val selectedBottomIcon: Color
)

private fun editMatchColors(isDarkMode: Boolean): EditMatchColors {
    return if (isDarkMode) {
        EditMatchColors(
            background = Color(0xFF111111),
            textPrimary = Color(0xFFEDEDED),
            textSecondary = Color(0xFFBDBDBD),
            textMuted = Color(0xFF9E9E9E),
            inputBackground = Color(0xFF1A1A1A),
            inputBorder = Color(0xFF3E463E),
            icon = Color(0xFFC9D1C9),
            headerIcon = Color(0xFFC9D1C9),
            accent = Color(0xFF9EF49B),
            accentText = Color(0xFF111111),
            disabledButton = Color(0xFF3A4A3A),
            segmentedBackground = Color(0xFF1A1A1A),
            optionBackground = Color(0xFF1A1A1A),
            optionBorder = Color(0xFF3E463E),
            infoBoxBackground = Color(0xFF163B73),
            infoBoxText = Color(0xFFDCEBFF),
            danger = Color(0xFFFF6B6B),
            bottomBarBackground = Color(0xFF151515),
            bottomIcon = Color(0xFFC9D1C9),
            selectedBottomIcon = Color(0xFF111111)
        )
    } else {
        EditMatchColors(
            background = Color(0xFFFAF9F8),
            textPrimary = Color(0xFF202020),
            textSecondary = Color(0xFF4F574C),
            textMuted = Color(0xFF6F6F6F),
            inputBackground = White,
            inputBorder = Color(0xFFCCD5DD),
            icon = Color(0xFF465046),
            headerIcon = GreenPrimary,
            accent = GreenPrimary,
            accentText = White,
            disabledButton = Color(0xFFB8C7B8),
            segmentedBackground = Color(0xFFEFEDED),
            optionBackground = White,
            optionBorder = Color(0xFFCCD5DD),
            infoBoxBackground = Color(0xFF0B5BCB),
            infoBoxText = White,
            danger = Color(0xFFD32F2F),
            bottomBarBackground = Color(0xFFF3F1F1),
            bottomIcon = Color(0xFF4F574C),
            selectedBottomIcon = Color(0xFFBDE8B9)
        )
    }
}

@Composable
fun EditMatchScreen(
    matchId: String,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onCancelMatchClick: () -> Unit,
    onHomeClick: () -> Unit,
    onMatchesClick: () -> Unit,
    onCreateMatchClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit = {},
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {},
    viewModel: PartidoViewModel = viewModel() // <- 1. Inyectamos ViewModel
) {
    // 2. Buscamos el partido actual
    val partidos by viewModel.partidos.collectAsState()
    val partidoActual = partidos.find { it.id == matchId }
    val uiState by viewModel.uiState.collectAsState()

    // 3. Variables de estado (arrancan vacías y se llenan con LaunchedEffect)
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("1500") }
    var players by remember { mutableStateOf("") }
    var level by remember { mutableStateOf("Medio") }

    var showDeleteConfirmation by remember { mutableStateOf(false) }

    var surface by remember { mutableStateOf("Sintético") }
    var description by remember { mutableStateOf("Llegar 15 minutos antes.") }

    // 4. Cargamos los datos reales cuando se encuentra el partido
    LaunchedEffect(partidoActual) {
        partidoActual?.let {
            title = it.titulo
            date = it.fecha
            time = it.hora
            location = it.lugar
            players = it.maxJugadores.toString()
        }
    }

    // 5. Escuchamos si se guardó o borró con éxito para navegar
    LaunchedEffect(uiState) {
        if (uiState is PartidoUiState.Success) {
            viewModel.resetState()
            onSaveClick() // Esto te devuelve a la lista de partidos
        }
    }

    val colors = editMatchColors(isDarkMode)

    Scaffold(
        bottomBar = {
            EditMatchBottomBar(
                colors = colors,
                onHomeClick = onHomeClick,
                onMatchesClick = onMatchesClick,
                onCreateMatchClick = onCreateMatchClick,
                onProfileClick = onProfileClick
            )
        },
        containerColor = colors.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(colors.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 10.dp)
        ) {
            EditMatchHeader(
                colors = colors,
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
                onBackClick = onBackClick,
                onNotificationsClick = onNotificationsClick
            )

            Spacer(modifier = Modifier.height(14.dp))

            EditMatchLabel(
                text = "Título del partido",
                colors = colors
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                colors = editMatchTextFieldColors(colors)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    EditMatchLabel(
                        text = "Fecha",
                        colors = colors
                    )

                    OutlinedTextField(
                        value = date,
                        onValueChange = { newValue ->
                            val formattedDate = formatDateInput(newValue)

                            if (isValidPartialDate(formattedDate)) {
                                date = formattedDate
                            }
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Fecha",
                                tint = colors.icon
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = editMatchTextFieldColors(colors)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    EditMatchLabel(
                        text = "Hora",
                        colors = colors
                    )

                    OutlinedTextField(
                        value = time,
                        onValueChange = { newValue ->
                            val formattedTime = formatTimeInput(newValue)

                            if (isValidPartialTime(formattedTime)) {
                                time = formattedTime
                            }
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "Hora",
                                tint = colors.icon
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = editMatchTextFieldColors(colors)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            EditMatchLabel(
                text = "Ubicación",
                colors = colors
            )

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "Ubicación",
                        tint = colors.icon
                    )
                },
                placeholder = {
                    Text(
                        text = "Busca una cancha o dirección",
                        color = colors.textMuted
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                colors = editMatchTextFieldColors(colors)
            )

            Spacer(modifier = Modifier.height(6.dp))

            EditMockLocationButton(
                colors = colors,
                onClick = {
                    location = "Cerca de Caballito, CABA"
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    EditMatchLabel(
                        text = "Precio por jugador",
                        colors = colors
                    )

                    OutlinedTextField(
                        value = price,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() } && newValue.length <= 6) {
                                price = newValue
                            }
                        },
                        leadingIcon = {
                            Text(
                                text = "$",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.icon
                            )
                        },
                        placeholder = {
                            Text(
                                text = "Ej: 1500",
                                color = colors.textMuted
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = editMatchTextFieldColors(colors)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    EditMatchLabel(
                        text = "Cantidad de jugadores",
                        colors = colors
                    )

                    OutlinedTextField(
                        value = players,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() } && newValue.length <= 2) {
                                players = newValue
                            }
                        },
                        placeholder = {
                            Text(
                                text = "Ej: 10",
                                color = colors.textMuted
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = editMatchTextFieldColors(colors)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "ℹ",
                    color = colors.accent,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Podés aumentar el cupo, pero no reducirlo por debajo de los jugadores anotados.",
                    color = colors.textSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            EditMatchLabel(
                text = "Nivel de juego",
                colors = colors
            )

            EditSegmentedOptions(
                options = listOf("Amateur", "Medio", "Avanzado"),
                selectedOption = level,
                colors = colors,
                onOptionSelected = { level = it }
            )

            Spacer(modifier = Modifier.height(18.dp))

            EditMatchLabel(
                text = "Superficie",
                colors = colors
            )

            EditChipOptions(
                options = listOf("Sintético", "Césped Natural", "Indoor"),
                selectedOption = surface,
                colors = colors,
                onOptionSelected = { surface = it }
            )

            Spacer(modifier = Modifier.height(18.dp))

            EditMatchLabel(
                text = "Descripción / Reglas",
                colors = colors
            )

            OutlinedTextField(
                value = description,
                onValueChange = {
                    if (it.length <= 300) {
                        description = it
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp),
                shape = RoundedCornerShape(10.dp),
                maxLines = 4,
                colors = editMatchTextFieldColors(colors)
            )

            Spacer(modifier = Modifier.height(22.dp))

            EditMatchInfoBox(colors = colors)

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = {
                    partidoActual?.let { p ->
                        val actualizado = p.copy(
                            titulo = title,
                            fecha = date,
                            hora = time,
                            lugar = location,
                            maxJugadores = players.toIntOrNull() ?: p.maxJugadores,
                            precio = price,
                            nivel = level,
                            superficie = surface,
                            descripcion = description
                        )
                        viewModel.actualizarPartido(actualizado)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(28.dp),
                enabled = isCompleteDate(date) && isCompleteTime(time),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.accent,
                    contentColor = colors.accentText,
                    disabledContainerColor = colors.disabledButton,
                    disabledContentColor = White
                )
            ) {
                Text(
                    text = "Guardar Cambios",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    // En vez de borrar directamente, mostramos el popup
                    showDeleteConfirmation = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, colors.danger),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colors.danger
                )
            ) {
                Text(
                    text = "Cancelar partido",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
    // Diálogo de confirmación
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            containerColor = colors.background,
            title = {
                Text(
                    text = "Cancelar partido",
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    fontSize = 20.sp
                )
            },
            text = {
                Text(
                    text = "¿Estás seguro de que querés cancelar este partido? Esta acción no se puede deshacer y se liberarán los cupos de todos los jugadores confirmados.",
                    color = colors.textSecondary,
                    fontSize = 15.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmation = false
                        viewModel.borrarPartido(matchId) // Recién acá borramos en Firebase
                    }
                ) {
                    Text("Sí, cancelar", color = colors.danger, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirmation = false }
                ) {
                    Text("Volver", color = colors.textPrimary, fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }
}


@Composable
private fun EditMockLocationButton(
    colors: EditMatchColors,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Place,
            contentDescription = "Usar mi ubicación actual",
            tint = colors.accent,
            modifier = Modifier.size(17.dp)
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = "Usar mi ubicación actual",
            color = colors.accent,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun EditMatchHeader(
    colors: EditMatchColors,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onBackClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = colors.headerIcon
            )
        }

        Text(
            text = "Editar Partido",
            color = colors.textPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

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
private fun EditMatchLabel(
    text: String,
    colors: EditMatchColors
) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = colors.textSecondary,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun EditSegmentedOptions(
    options: List<String>,
    selectedOption: String,
    colors: EditMatchColors,
    onOptionSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(colors.segmentedBackground)
            .padding(3.dp)
    ) {
        options.forEach { option ->
            val selected = selectedOption == option

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        if (selected) colors.accent else Color.Transparent
                    )
                    .clickable { onOptionSelected(option) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    color = if (selected) colors.accentText else colors.textSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun EditChipOptions(
    options: List<String>,
    selectedOption: String,
    colors: EditMatchColors,
    onOptionSelected: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            val selected = selectedOption == option

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (selected) colors.accent else colors.optionBackground,
                border = BorderStroke(
                    width = 1.dp,
                    color = if (selected) colors.accent else colors.optionBorder
                ),
                modifier = Modifier.clickable { onOptionSelected(option) }
            ) {
                Text(
                    text = option,
                    fontSize = 12.sp,
                    color = if (selected) colors.accentText else colors.textSecondary,
                    modifier = Modifier.padding(horizontal = 13.dp, vertical = 9.dp)
                )
            }
        }
    }
}

@Composable
private fun EditMatchInfoBox(
    colors: EditMatchColors
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.infoBoxBackground
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "ℹ",
                color = colors.infoBoxText,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Al guardar los cambios o cancelar el partido, se notificará automáticamente a todos los jugadores confirmados.",
                color = colors.infoBoxText,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun editMatchTextFieldColors(
    colors: EditMatchColors
) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = colors.accent,
    unfocusedBorderColor = colors.inputBorder,
    focusedTextColor = colors.textPrimary,
    unfocusedTextColor = colors.textPrimary,
    focusedContainerColor = colors.inputBackground,
    unfocusedContainerColor = colors.inputBackground,
    cursorColor = colors.accent,
    focusedPlaceholderColor = colors.textMuted,
    unfocusedPlaceholderColor = colors.textMuted
)

private fun formatDateInput(value: String): String {
    val digits = value.filter { it.isDigit() }.take(8)

    return buildString {
        digits.forEachIndexed { index, char ->
            if (index == 2 || index == 4) {
                append("/")
            }
            append(char)
        }
    }
}

private fun isValidPartialDate(value: String): Boolean {
    val parts = value.split("/")

    val day = parts.getOrNull(0)
    val month = parts.getOrNull(1)

    if (day != null && day.length == 2) {
        val dayNumber = day.toIntOrNull() ?: return false
        if (dayNumber !in 1..31) return false
    }

    if (month != null && month.length == 2) {
        val monthNumber = month.toIntOrNull() ?: return false
        if (monthNumber !in 1..12) return false
    }

    return true
}

private fun isCompleteDate(value: String): Boolean {
    if (value.length != 10) return false

    val parts = value.split("/")
    if (parts.size != 3) return false

    val day = parts[0].toIntOrNull() ?: return false
    val month = parts[1].toIntOrNull() ?: return false
    val year = parts[2].toIntOrNull() ?: return false

    return day in 1..31 && month in 1..12 && year in 2023..2100
}

private fun formatTimeInput(value: String): String {
    val digits = value.filter { it.isDigit() }.take(4)

    return buildString {
        digits.forEachIndexed { index, char ->
            if (index == 2) {
                append(":")
            }
            append(char)
        }
    }
}

private fun isValidPartialTime(value: String): Boolean {
    val parts = value.split(":")

    val hour = parts.getOrNull(0)
    val minutes = parts.getOrNull(1)

    if (hour != null && hour.length == 2) {
        val hourNumber = hour.toIntOrNull() ?: return false
        if (hourNumber !in 0..23) return false
    }

    if (minutes != null && minutes.length == 2) {
        val minuteNumber = minutes.toIntOrNull() ?: return false
        if (minuteNumber !in 0..59) return false
    }

    return true
}

private fun isCompleteTime(value: String): Boolean {
    if (value.length != 5) return false

    val parts = value.split(":")
    if (parts.size != 2) return false

    val hour = parts[0].toIntOrNull() ?: return false
    val minutes = parts[1].toIntOrNull() ?: return false

    return hour in 0..23 && minutes in 0..59
}

@Composable
private fun EditMatchBottomBar(
    colors: EditMatchColors,
    onHomeClick: () -> Unit,
    onMatchesClick: () -> Unit,
    onCreateMatchClick: () -> Unit,
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
            EditMatchBottomItem(
                icon = Icons.Default.Home,
                label = "Inicio",
                selected = false,
                colors = colors,
                onClick = onHomeClick
            )

            EditMatchBottomItem(
                iconText = "⚽",
                label = "Partidos",
                selected = true,
                colors = colors,
                onClick = onMatchesClick
            )

            EditMatchBottomItem(
                icon = Icons.Default.Add,
                label = "Crear",
                selected = false,
                colors = colors,
                onClick = onCreateMatchClick
            )

            EditMatchBottomItem(
                icon = Icons.Default.Person,
                label = "Perfil",
                selected = false,
                colors = colors,
                onClick = onProfileClick
            )
        }
    }
}

@Composable
private fun EditMatchBottomItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    colors: EditMatchColors,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(76.dp)
            .height(66.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(if (selected) colors.accent else Color.Transparent)
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
                tint = if (selected) colors.selectedBottomIcon else colors.bottomIcon,
                modifier = Modifier.size(26.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = label,
                color = if (selected) colors.selectedBottomIcon else colors.bottomIcon,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun EditMatchBottomItem(
    iconText: String,
    label: String,
    selected: Boolean,
    colors: EditMatchColors,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(76.dp)
            .height(66.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(if (selected) colors.accent else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = iconText,
                fontSize = 24.sp,
                color = if (selected) colors.selectedBottomIcon else colors.bottomIcon
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = label,
                color = if (selected) colors.selectedBottomIcon else colors.bottomIcon,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}