package com.matchball.fulbomatch.ui.screens

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
import com.matchball.fulbomatch.ui.theme.GreenPrimary
import com.matchball.fulbomatch.ui.theme.White

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
    onNotificationsClick: () -> Unit = {}
) {
    val initialTitle = when (matchId) {
        "2" -> "Fútbol 7 Mix"
        "past_3" -> "Fútbol City"
        else -> "Fútbol 7 - Los Pibes"
    }

    var title by remember { mutableStateOf(initialTitle) }
    var date by remember { mutableStateOf("15/11/2023") }
    var time by remember { mutableStateOf("20:00") }
    var location by remember { mutableStateOf("Canchas \"El Templo\", Av. Libertador 1200") }
    var price by remember { mutableStateOf("1500") }
    var players by remember { mutableStateOf("10") }
    var level by remember { mutableStateOf("Medio") }
    var surface by remember { mutableStateOf("Sintético") }
    var description by remember {
        mutableStateOf("Llegar 15 minutos antes. Se suspende por lluvia fuerte. Traer camiseta blanca o negra.")
    }

    Scaffold(
        bottomBar = {
            EditMatchBottomBar(
                onHomeClick = onHomeClick,
                onMatchesClick = onMatchesClick,
                onCreateMatchClick = onCreateMatchClick,
                onProfileClick = onProfileClick
            )
        },
        containerColor = Color(0xFFFAF9F8)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFAF9F8))
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 10.dp)
        ) {
            EditMatchHeader(
                onBackClick = onBackClick,
                onNotificationsClick = onNotificationsClick
            )

            Spacer(modifier = Modifier.height(14.dp))

            EditMatchLabel("Título del partido")

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                colors = editMatchTextFieldColors()
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    EditMatchLabel("Fecha")

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
                                tint = Color(0xFF465046)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = editMatchTextFieldColors()
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    EditMatchLabel("Hora")

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
                                tint = Color(0xFF465046)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = editMatchTextFieldColors()
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            EditMatchLabel("Ubicación")

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "Ubicación",
                        tint = Color(0xFF465046)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                colors = editMatchTextFieldColors()
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    EditMatchLabel("Precio por jugador")

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
                                color = Color(0xFF465046)
                            )
                        },
                        placeholder = {
                            Text(text = "Ej: 1500")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = editMatchTextFieldColors()
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    EditMatchLabel("Cantidad de jugadores")

                    OutlinedTextField(
                        value = players,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() } && newValue.length <= 2) {
                                players = newValue
                            }
                        },
                        placeholder = {
                            Text("Ej: 10")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        colors = editMatchTextFieldColors()
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "ℹ",
                    color = GreenPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Podés aumentar el cupo, pero no reducirlo por debajo de los jugadores anotados.",
                    color = Color(0xFF4F574C),
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            EditMatchLabel("Nivel de juego")

            EditSegmentedOptions(
                options = listOf("Amateur", "Medio", "Avanzado"),
                selectedOption = level,
                onOptionSelected = { level = it }
            )

            Spacer(modifier = Modifier.height(18.dp))

            EditMatchLabel("Superficie")

            EditChipOptions(
                options = listOf("Sintético", "Césped Natural", "Indoor"),
                selectedOption = surface,
                onOptionSelected = { surface = it }
            )

            Spacer(modifier = Modifier.height(18.dp))

            EditMatchLabel("Descripción / Reglas")

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
                colors = editMatchTextFieldColors()
            )

            Spacer(modifier = Modifier.height(22.dp))

            EditMatchInfoBox()

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(28.dp),
                enabled = isCompleteDate(date) && isCompleteTime(time),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenPrimary,
                    contentColor = White,
                    disabledContainerColor = Color(0xFFB8C7B8),
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
                onClick = onCancelMatchClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, Color(0xFFD32F2F)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFD32F2F)
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
}

@Composable
private fun EditMatchHeader(
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
                tint = GreenPrimary
            )
        }

        Text(
            text = "Editar Partido",
            color = GreenPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = { }) {
            Text(
                text = "☾",
                fontSize = 23.sp,
                color = GreenPrimary
            )
        }

        IconButton(onClick = onNotificationsClick) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notificaciones",
                tint = GreenPrimary
            )
        }
    }
}

@Composable
private fun EditMatchLabel(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF4A4A4A),
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun EditSegmentedOptions(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFEFEDED))
            .padding(3.dp)
    ) {
        options.forEach { option ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(42.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        if (selectedOption == option) GreenPrimary else Color.Transparent
                    )
                    .clickable { onOptionSelected(option) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option,
                    color = if (selectedOption == option) White else Color(0xFF4F574C),
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
    onOptionSelected: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (selectedOption == option) GreenPrimary else Color.White,
                border = BorderStroke(
                    width = 1.dp,
                    color = if (selectedOption == option) GreenPrimary else Color(0xFFCCD5DD)
                ),
                modifier = Modifier.clickable { onOptionSelected(option) }
            ) {
                Text(
                    text = option,
                    fontSize = 12.sp,
                    color = if (selectedOption == option) White else Color(0xFF4F574C),
                    modifier = Modifier.padding(horizontal = 13.dp, vertical = 9.dp)
                )
            }
        }
    }
}

@Composable
private fun EditMatchInfoBox() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0B5BCB)
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "ℹ",
                color = Color(0xFFDCEBFF),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = "Al guardar los cambios o cancelar el partido, se notificará automáticamente a todos los jugadores confirmados.",
                color = White,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun editMatchTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = GreenPrimary,
    unfocusedBorderColor = Color(0xFFCCD5DD),
    focusedTextColor = Color.Black,
    unfocusedTextColor = Color.Black,
    focusedContainerColor = White,
    unfocusedContainerColor = White,
    cursorColor = GreenPrimary,
    focusedPlaceholderColor = Color(0xFF8A8A8A),
    unfocusedPlaceholderColor = Color(0xFF8A8A8A)
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
        color = Color(0xFFF3F1F1),
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
                onClick = onHomeClick
            )

            EditMatchBottomItem(
                iconText = "⚽",
                label = "Partidos",
                selected = true,
                onClick = onMatchesClick
            )

            EditMatchBottomItem(
                icon = Icons.Default.Add,
                label = "Crear",
                selected = false,
                onClick = onCreateMatchClick
            )

            EditMatchBottomItem(
                icon = Icons.Default.Person,
                label = "Perfil",
                selected = false,
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
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(76.dp)
            .height(66.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(if (selected) GreenPrimary else Color.Transparent)
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
                tint = if (selected) Color(0xFFBDE8B9) else Color(0xFF4F574C),
                modifier = Modifier.size(26.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = label,
                color = if (selected) Color(0xFFBDE8B9) else Color(0xFF4F574C),
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
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(76.dp)
            .height(66.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(if (selected) GreenPrimary else Color.Transparent)
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
                color = if (selected) Color(0xFFBDE8B9) else Color(0xFF4F574C)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = label,
                color = if (selected) Color(0xFFBDE8B9) else Color(0xFF4F574C),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}