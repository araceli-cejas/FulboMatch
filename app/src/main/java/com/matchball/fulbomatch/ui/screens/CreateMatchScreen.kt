package com.matchball.fulbomatch.ui.screens

import androidx.compose.foundation.BorderStroke
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
import com.matchball.fulbomatch.ui.components.MoonIconButton
import com.matchball.fulbomatch.ui.theme.GreenPrimary
import com.matchball.fulbomatch.ui.theme.White
import kotlin.math.roundToInt

private data class CreateMatchColors(
    val background: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val placeholder: Color,
    val inputBackground: Color,
    val inputBorder: Color,
    val icon: Color,
    val headerIcon: Color,
    val accent: Color,
    val accentText: Color,
    val chipBackground: Color,
    val chipSelectedBackground: Color,
    val chipBorder: Color,
    val sliderInactive: Color,
    val bottomBarBackground: Color,
    val bottomIcon: Color,
    val selectedBottomIcon: Color
)

private fun createMatchColors(isDarkMode: Boolean): CreateMatchColors {
    return if (isDarkMode) {
        CreateMatchColors(
            background = Color(0xFF111111),
            textPrimary = Color(0xFFEDEDED),
            textSecondary = Color(0xFFBDBDBD),
            placeholder = Color(0xFF9E9E9E),
            inputBackground = Color(0xFF1A1A1A),
            inputBorder = Color(0xFF3E463E),
            icon = Color(0xFFC9D1C9),
            headerIcon = Color(0xFFC9D1C9),
            accent = Color(0xFF9EF49B),
            accentText = Color(0xFF111111),
            chipBackground = Color(0xFF1A1A1A),
            chipSelectedBackground = Color(0xFF9EF49B),
            chipBorder = Color(0xFF3E463E),
            sliderInactive = Color(0xFF333333),
            bottomBarBackground = Color(0xFF151515),
            bottomIcon = Color(0xFFC9D1C9),
            selectedBottomIcon = Color(0xFF111111)
        )
    } else {
        CreateMatchColors(
            background = Color(0xFFF6F6F6),
            textPrimary = Color(0xFF202020),
            textSecondary = Color(0xFF4F574C),
            placeholder = Color(0xFF8A8A8A),
            inputBackground = White,
            inputBorder = Color(0xFFBDBDBD),
            icon = Color(0xFF455044),
            headerIcon = GreenPrimary,
            accent = GreenPrimary,
            accentText = White,
            chipBackground = White,
            chipSelectedBackground = Color(0xFFE2E2E2),
            chipBorder = Color(0xFFD0D0D0),
            sliderInactive = Color(0xFFE0E0E0),
            bottomBarBackground = Color(0xFFF3F1F1),
            bottomIcon = Color(0xFF4F574C),
            selectedBottomIcon = Color(0xFFBDE8B9)
        )
    }
}

@Composable
fun CreateMatchScreen(
    onMatchCreated: () -> Unit,
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit = {},
    onMatchesClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var maxPlayers by remember { mutableFloatStateOf(10f) }
    var selectedLevel by remember { mutableStateOf("Intermedio") }
    var description by remember { mutableStateOf("") }

    val colors = createMatchColors(isDarkMode)

    Scaffold(
        bottomBar = {
            CreateMatchBottomBar(
                colors = colors,
                onHomeClick = onBackClick,
                onMatchesClick = onMatchesClick,
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
                .padding(horizontal = 22.dp, vertical = 12.dp)
                .semantics { contentDescription = "Formulario para crear partido" }
        ) {
            CreateMatchHeader(
                colors = colors,
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
                onNotificationsClick = onNotificationsClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Crear nuevo partido",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(24.dp))

            FormLabel(
                text = "Título del partido",
                colors = colors
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = {
                    Text(
                        text = "Ej: Futbol 5 en Palermo",
                        color = colors.placeholder
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                colors = formTextFieldColors(colors)
            )

            Spacer(modifier = Modifier.height(18.dp))

            FormLabel(
                text = "Fecha",
                colors = colors
            )

            OutlinedTextField(
                value = date,
                onValueChange = { date = formatDateInput(it) },
                placeholder = {
                    Text(
                        text = "dd/mm/yyyy",
                        color = colors.placeholder
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Fecha",
                        tint = colors.icon,
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                colors = formTextFieldColors(colors)
            )

            Spacer(modifier = Modifier.height(18.dp))

            FormLabel(
                text = "Hora",
                colors = colors
            )

            OutlinedTextField(
                value = time,
                onValueChange = { time = formatTimeInput(it) },
                placeholder = {
                    Text(
                        text = "hh:mm",
                        color = colors.placeholder
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Hora",
                        tint = colors.icon,
                        modifier = Modifier.size(22.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                colors = formTextFieldColors(colors)
            )

            Spacer(modifier = Modifier.height(18.dp))

            FormLabel(
                text = "Zona / Ubicación",
                colors = colors
            )

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                placeholder = {
                    Text(
                        text = "Busca una cancha o dirección",
                        color = colors.placeholder
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "Ubicación",
                        tint = colors.icon,
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                colors = formTextFieldColors(colors)
            )

            Spacer(modifier = Modifier.height(6.dp))

            MockLocationButton(
                colors = colors,
                onClick = {
                    location = "Caballito, CABA"
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            FormLabel(
                text = "Precio por jugador",
                colors = colors
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it.filter { char -> char.isDigit() } },
                placeholder = {
                    Text(
                        text = "$ 1500",
                        color = colors.placeholder
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Payments,
                        contentDescription = "Precio",
                        tint = colors.icon,
                        modifier = Modifier.size(22.dp)
                    )
                },
                prefix = {
                    Text(
                        text = "$ ",
                        color = colors.textPrimary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                colors = formTextFieldColors(colors)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FormLabel(
                    text = "Máximo de jugadores",
                    colors = colors,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = maxPlayers.roundToInt().toString(),
                    color = colors.accent,
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
                    thumbColor = colors.accent,
                    activeTrackColor = colors.accent,
                    inactiveTrackColor = colors.sliderInactive
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            FormLabel(
                text = "Nivel requerido",
                colors = colors
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LevelChip(
                    text = "Principiante",
                    selected = selectedLevel == "Principiante",
                    colors = colors,
                    onClick = { selectedLevel = "Principiante" }
                )

                LevelChip(
                    text = "Intermedio",
                    selected = selectedLevel == "Intermedio",
                    colors = colors,
                    onClick = { selectedLevel = "Intermedio" }
                )

                LevelChip(
                    text = "Avanzado",
                    selected = selectedLevel == "Avanzado",
                    colors = colors,
                    onClick = { selectedLevel = "Avanzado" }
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            FormLabel(
                text = "Descripción (Opcional)",
                colors = colors
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = {
                    Text(
                        text = "Añade detalles sobre el partido, reglas,\netc.",
                        color = colors.placeholder
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                shape = RoundedCornerShape(8.dp),
                maxLines = 4,
                colors = formTextFieldColors(colors)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = { onMatchCreated() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.accent,
                    contentColor = colors.accentText
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
private fun MockLocationButton(
    colors: CreateMatchColors,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 2.dp)
            .semantics { contentDescription = "Usar mi ubicación actual" },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Place,
            contentDescription = null,
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
private fun CreateMatchHeader(
    colors: CreateMatchColors,
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
        modifier = Modifier.fillMaxWidth(),
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
private fun FormLabel(
    text: String,
    colors: CreateMatchColors,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = colors.textPrimary,
        modifier = modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun LevelChip(
    text: String,
    selected: Boolean,
    colors: CreateMatchColors,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(36.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        color = if (selected) colors.chipSelectedBackground else colors.chipBackground,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) colors.accent else colors.chipBorder
        )
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 13.sp,
                color = if (selected) colors.accentText else colors.textPrimary,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun formTextFieldColors(
    colors: CreateMatchColors
) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = colors.accent,
    unfocusedBorderColor = colors.inputBorder,
    focusedTextColor = colors.textPrimary,
    unfocusedTextColor = colors.textPrimary,
    focusedPlaceholderColor = colors.placeholder,
    unfocusedPlaceholderColor = colors.placeholder,
    cursorColor = colors.accent,
    focusedContainerColor = colors.inputBackground,
    unfocusedContainerColor = colors.inputBackground
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
    colors: CreateMatchColors,
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
                colors = colors,
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
    colors: CreateMatchColors,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(76.dp)
            .height(66.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(colors.accent)
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
                tint = colors.selectedBottomIcon,
                modifier = Modifier.size(26.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = label,
                color = colors.selectedBottomIcon,
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
    colors: CreateMatchColors,
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
    colors: CreateMatchColors,
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