package com.matchball.fulbomatch.ui.screens

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matchball.fulbomatch.ui.theme.GrayMedium
import com.matchball.fulbomatch.ui.theme.GreenPrimary
import com.matchball.fulbomatch.ui.theme.White
import com.matchball.fulbomatch.ui.components.MoonIconButton
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit = {},
    onHomeClick: () -> Unit = onBackClick,
    onMatchesClick: () -> Unit = {},
    onCreateMatchClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {}
) {
    var fullName by remember { mutableStateOf("Lucas Fernández") }
    var email by remember { mutableStateOf("lucasfer@gmail.com") }
    var phone by remember { mutableStateOf("+34 600 123 456") }
    var age by remember { mutableStateOf("24") }
    var zone by remember { mutableStateOf("Caballito, CABA") }
    var position by remember { mutableStateOf("Arquero") }
    var description by remember {
        mutableStateOf("Jugador de equipo, me gusta distribuir el balón y mantener el ritmo en el centro del campo. Disponible los fines de semana.")
    }

    val positions = listOf(
        "Arquero",
        "Defensor",
        "Mediocampista",
        "Delantero"
    )

    Scaffold(
        bottomBar = {
            EditProfileBottomBar(
                onHomeClick = onHomeClick,
                onMatchesClick = onMatchesClick,
                onCreateMatchClick = onCreateMatchClick
            )
        },
        containerColor = Color(0xFFF6F8FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF6F8FA))
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .semantics { contentDescription = "Pantalla de editar perfil" }
        ) {
            EditProfileHeader(
                onBackClick = onBackClick,
                onNotificationsClick = onNotificationsClick
            )

            Spacer(modifier = Modifier.height(22.dp))

            EditProfileAvatar()

            Spacer(modifier = Modifier.height(22.dp))

            FormLabel("Nombre completo")

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Nombre",
                        tint = Color(0xFF465046),
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                colors = profileTextFieldColors()
            )

            Spacer(modifier = Modifier.height(18.dp))

            FormLabel("Correo electrónico")

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Correo electrónico",
                        tint = Color(0xFF465046),
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = profileTextFieldColors()
            )

            Spacer(modifier = Modifier.height(18.dp))

            FormLabel("Teléfono")

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Teléfono",
                        tint = Color(0xFF465046),
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = profileTextFieldColors()
            )

            Spacer(modifier = Modifier.height(18.dp))

            FormLabel("Edad")

            OutlinedTextField(
                value = age,
                onValueChange = { newValue ->
                    if (newValue.length <= 2 && newValue.all { it.isDigit() }) {
                        age = newValue
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Edad",
                        tint = Color(0xFF465046),
                        modifier = Modifier.size(18.dp)
                    )
                },
                placeholder = {
                    Text(text = "Ej: 28")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = profileTextFieldColors()
            )

            Spacer(modifier = Modifier.height(18.dp))

            FormLabel("Zona o barrio")

            OutlinedTextField(
                value = zone,
                onValueChange = { zone = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "Zona o barrio",
                        tint = Color(0xFF465046),
                        modifier = Modifier.size(18.dp)
                    )
                },
                placeholder = {
                    Text(text = "Ej: Palermo, CABA")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                colors = profileTextFieldColors()
            )

            Spacer(modifier = Modifier.height(18.dp))

            FormLabel("Posición preferida")

            PositionDropdown(
                selectedPosition = position,
                positions = positions,
                onPositionSelected = { position = it }
            )

            Spacer(modifier = Modifier.height(18.dp))

            FormLabel("Breve descripción")

            OutlinedTextField(
                value = description,
                onValueChange = {
                    if (it.length <= 300) {
                        description = it
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp),
                shape = RoundedCornerShape(10.dp),
                maxLines = 5,
                colors = profileTextFieldColors()
            )

            Text(
                text = "${description.length} / 300",
                fontSize = 11.sp,
                color = GrayMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                textAlign = TextAlign.End
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenPrimary,
                    contentColor = White
                )
            ) {
                Text(
                    text = "Guardar Cambios",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(38.dp))
        }
    }
}

@Composable
private fun EditProfileHeader(
    onBackClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = Color.Black
            )
        }

        Text(
            text = "Editar perfil",
            color = GreenPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        MoonIconButton()

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
private fun EditProfileAvatar() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(118.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier
                    .size(112.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE4E8E5)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto de perfil",
                    tint = GreenPrimary,
                    modifier = Modifier.size(62.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(GreenPrimary)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar foto",
                    tint = White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Toca para cambiar la foto",
            fontSize = 12.sp,
            color = Color(0xFF637063)
        )
    }
}

@Composable
private fun FormLabel(
    text: String
) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFF202020),
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PositionDropdown(
    selectedPosition: String,
    positions: List<String>,
    onPositionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedPosition,
            onValueChange = {},
            readOnly = true,
            leadingIcon = {
                Text(
                    text = "⚽",
                    fontSize = 18.sp
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Abrir opciones",
                    tint = Color(0xFF465046)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(
                    type = MenuAnchorType.PrimaryNotEditable,
                    enabled = true
                ),
            shape = RoundedCornerShape(10.dp),
            singleLine = true,
            colors = profileTextFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 240.dp),
            containerColor = White
        ) {
            positions.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = Color(0xFF202020),
                            fontSize = 16.sp
                        )
                    },
                    onClick = {
                        onPositionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun profileTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = GreenPrimary,
    unfocusedBorderColor = Color(0xFFCCD5DD),
    focusedTextColor = Color.Black,
    unfocusedTextColor = Color.Black,
    focusedContainerColor = White,
    unfocusedContainerColor = White,
    cursorColor = GreenPrimary,
    focusedPlaceholderColor = GrayMedium,
    unfocusedPlaceholderColor = GrayMedium
)

@Composable
private fun EditProfileBottomBar(
    onHomeClick: () -> Unit,
    onMatchesClick: () -> Unit,
    onCreateMatchClick: () -> Unit
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

            UnselectedBottomItem(
                icon = Icons.Default.Add,
                label = "Crear",
                onClick = onCreateMatchClick
            )

            SelectedBottomItem(
                icon = Icons.Default.Person,
                label = "Perfil",
                onClick = { }
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