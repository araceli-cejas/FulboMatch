package com.matchball.fulbomatch.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matchball.fulbomatch.R
import com.matchball.fulbomatch.ui.theme.GrayMedium
import com.matchball.fulbomatch.ui.theme.GreenPrimary
import com.matchball.fulbomatch.ui.theme.White
import kotlin.math.roundToInt

@Composable
fun CreateMatchScreen(
    onMatchCreated: () -> Unit,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit = {},
    onMatchesClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var maxPlayers by remember { mutableFloatStateOf(10f) }
    var selectedLevel by remember { mutableStateOf("Intermedio") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            CreateMatchBottomBar(
                onHomeClick = onBackClick,
                onMatchesClick = onMatchesClick,
                onProfileClick = onProfileClick
            )
        },
        containerColor = Color(0xFFF6F6F6)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF6F6F6))
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 12.dp)
                .semantics { contentDescription = "Formulario para crear partido" }
        ) {
            CreateMatchHeader(
                onNotificationsClick = onNotificationsClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Crear nuevo partido",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF202020)
            )

            Spacer(modifier = Modifier.height(24.dp))

            FormLabel("Título del partido")

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = {
                    Text(
                        text = "Ej: Futbol 5 en Palermo",
                        color = GrayMedium
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                colors = formTextFieldColors()
            )

            Spacer(modifier = Modifier.height(18.dp))

            FormLabel("Fecha")

            OutlinedTextField(
                value = date,
                onValueChange = { date = formatDateInput(it) },
                placeholder = {
                    Text(
                        text = "dd/mm/yyyy",
                        color = GrayMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Fecha",
                        tint = Color(0xFF455044),
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                colors = formTextFieldColors()
            )

            Spacer(modifier = Modifier.height(18.dp))

            FormLabel("Hora")

            OutlinedTextField(
                value = time,
                onValueChange = { time = formatTimeInput(it) },
                placeholder = {
                    Text(
                        text = "hh:mm",
                        color = GrayMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Hora",
                        tint = Color(0xFF455044),
                        modifier = Modifier.size(22.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                colors = formTextFieldColors()
            )

            Spacer(modifier = Modifier.height(18.dp))

            FormLabel("Zona / Ubicación")

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                placeholder = {
                    Text(
                        text = "Busca una cancha o dirección",
                        color = GrayMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "Ubicación",
                        tint = Color(0xFF455044),
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                colors = formTextFieldColors()
            )

            Spacer(modifier = Modifier.height(18.dp))

            FormLabel("Precio por jugador")

            OutlinedTextField(
                value = price,
                onValueChange = { price = it.filter { char -> char.isDigit() } },
                placeholder = {
                    Text(
                        text = "$ 1500",
                        color = GrayMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Payments,
                        contentDescription = "Precio",
                        tint = Color(0xFF455044),
                        modifier = Modifier.size(22.dp)
                    )
                },
                prefix = {
                    Text(
                        text = "$ ",
                        color = Color.Black
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                colors = formTextFieldColors()
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FormLabel(
                    text = "Máximo de jugadores",
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = maxPlayers.roundToInt().toString(),
                    color = GreenPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Slider(
                value = maxPlayers,
                onValueChange = { maxPlayers = it },
                valueRange = 2f..22f,
                steps = 19,
                colors = SliderDefaults.colors(
                    thumbColor = GreenPrimary,
                    activeTrackColor = GreenPrimary,
                    inactiveTrackColor = Color(0xFFE0E0E0)
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            FormLabel("Nivel requerido")

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LevelChip(
                    text = "Principiante",
                    selected = selectedLevel == "Principiante",
                    onClick = { selectedLevel = "Principiante" }
                )

                LevelChip(
                    text = "Intermedio",
                    selected = selectedLevel == "Intermedio",
                    onClick = { selectedLevel = "Intermedio" }
                )

                LevelChip(
                    text = "Avanzado",
                    selected = selectedLevel == "Avanzado",
                    onClick = { selectedLevel = "Avanzado" }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            FormLabel("Descripción (Opcional)")

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = {
                    Text(
                        text = "Añade detalles sobre el partido, reglas,\netc.",
                        color = GrayMedium
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                shape = RoundedCornerShape(8.dp),
                maxLines = 4,
                colors = formTextFieldColors()
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = { onMatchCreated() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenPrimary,
                    contentColor = White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear partido",
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Crear partido",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun CreateMatchHeader(
    onNotificationsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.nombre),
            contentDescription = "Logo FulboMatch",
            modifier = Modifier
                .width(155.dp)
                .height(42.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = { }) {
            Text(
                text = "☾",
                fontSize = 25.sp,
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
private fun FormLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF222222),
        modifier = modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun LevelChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(36.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = if (selected) Color(0xFFE2E2E2) else White,
        border = ButtonDefaults.outlinedButtonBorder(enabled = true)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 13.sp,
                color = Color(0xFF222222),
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun formTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = GreenPrimary,
    unfocusedBorderColor = Color(0xFFBDBDBD),
    focusedTextColor = Color.Black,
    unfocusedTextColor = Color.Black,
    focusedPlaceholderColor = GrayMedium,
    unfocusedPlaceholderColor = GrayMedium,
    cursorColor = GreenPrimary,
    focusedContainerColor = White,
    unfocusedContainerColor = White
)

private fun formatDateInput(input: String): String {
    val digits = input.filter { it.isDigit() }.take(8)

    return buildString {
        digits.forEachIndexed { index, char ->
            append(char)

            if (index == 1 && digits.length > 2) {
                append("/")
            }

            if (index == 3 && digits.length > 4) {
                append("/")
            }
        }
    }
}

private fun formatTimeInput(input: String): String {
    val digits = input.filter { it.isDigit() }.take(4)

    return buildString {
        digits.forEachIndexed { index, char ->
            append(char)

            if (index == 1 && digits.length > 2) {
                append(":")
            }
        }
    }
}

@Composable
private fun CreateMatchBottomBar(
    onHomeClick: () -> Unit,
    onMatchesClick: () -> Unit,
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
            UnselectedBottomItem(
                icon = Icons.Default.Home,
                label = "Inicio",
                onClick = onHomeClick
            )

            UnselectedBottomItem(
                iconText = "⚽",
                label = "Partidos",
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
            tint = Color(0xFF4F574C),
            modifier = Modifier.size(26.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = Color(0xFF4F574C),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun UnselectedBottomItem(
    iconText: String,
    label: String,
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
            color = Color(0xFF4F574C)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = Color(0xFF4F574C),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}