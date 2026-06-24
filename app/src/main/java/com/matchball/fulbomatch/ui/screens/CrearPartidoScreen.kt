package com.matchball.fulbomatch.ui.screens

import androidx.compose.foundation.Image
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
import com.matchball.fulbomatch.R
import com.matchball.fulbomatch.ui.theme.GreenPrimary
import com.matchball.fulbomatch.ui.partido.PartidoUiState
import com.matchball.fulbomatch.ui.partido.PartidoViewModel
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.matchball.fulbomatch.ui.components.MoonIconButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearPartidoScreen(
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onMatchesClick: () -> Unit,
    onProfileClick: () -> Unit,
    onPartidoCreado: () -> Unit,
    onToggleDarkMode: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
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
    val snackbarHostState = remember { SnackbarHostState() }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()
    val camposIncompletosMsg = stringResource(id = R.string.error_campos_incompletos)

    LaunchedEffect(uiState) {
        when (uiState) {
            is PartidoUiState.Success -> {
                viewModel.resetState()
                onPartidoCreado()
            }
            is PartidoUiState.Error -> {
                val errorMessage = (uiState as PartidoUiState.Error).message
                snackbarHostState.showSnackbar(message = errorMessage)
                viewModel.resetState()
            }
            else -> {}
        }
    }

    val topBarBg = if (isDarkMode) Color(0xFF111111) else Color.White
    val topBarContent = if (isDarkMode) Color.White else Color.Black
    val logoRes = if (isDarkMode) R.drawable.logo_dark else R.drawable.nombre

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    // Logo idéntico al de la pantalla de Partidos
                    Image(
                        painter = painterResource(id = logoRes),
                        contentDescription = "Logo FulboMatch",
                        modifier = Modifier
                            .width(190.dp)
                            .height(42.dp),
                        contentScale = ContentScale.Fit
                    )
                },
                actions = {
                    MoonIconButton(
                        isDarkMode = isDarkMode,
                        iconColor = GreenPrimary,
                        onClick = onToggleDarkMode
                    )
                    IconButton(onClick = onNotificationsClick) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificaciones",
                            tint = GreenPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topBarBg,
                    titleContentColor = topBarContent
                )
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Título en el cuerpo de la pantalla
                Text(
                    text = stringResource(id = R.string.title_organizar_partido),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = topBarContent
                )

                Spacer(modifier = Modifier.height(22.dp))

                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it; errorLocal = null },
                    label = { Text(stringResource(id = R.string.label_titulo_partido)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = fecha,
                    onValueChange = { },
                    label = { Text(stringResource(id = R.string.label_fecha)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true },
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = hora,
                    onValueChange = { },
                    label = { Text(stringResource(id = R.string.label_hora)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTimePicker = true },
                    enabled = false,
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
                    label = { Text(stringResource(id = R.string.label_lugar)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = maxJugadores,
                    onValueChange = { maxJugadores = it; errorLocal = null },
                    label = { Text(stringResource(id = R.string.label_max_jugadores)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (errorLocal != null) {
                    Text(errorLocal!!, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = {
                        if (titulo.isBlank() || fecha.isBlank() || hora.isBlank() || lugar.isBlank() || maxJugadores.isBlank()) {
                            errorLocal = camposIncompletosMsg
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
                    Text(stringResource(id = R.string.btn_crear_partido), fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }

            if (uiState is PartidoUiState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable(enabled = false) {},
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = GreenPrimary,
                        strokeWidth = 4.dp
                    )
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        fecha = formatter.format(Date(millis))
                    }
                    showDatePicker = false
                    errorLocal = null
                }) { Text(stringResource(id = R.string.btn_aceptar)) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text(stringResource(id = R.string.btn_cancelar)) }
            }
        ) { DatePicker(state = datePickerState) }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val h = timePickerState.hour.toString().padStart(2, '0')
                    val m = timePickerState.minute.toString().padStart(2, '0')
                    hora = "$h:$m"
                    showTimePicker = false
                    errorLocal = null
                }) { Text(stringResource(id = R.string.btn_aceptar)) }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text(stringResource(id = R.string.btn_cancelar)) }
            },
            text = { TimePicker(state = timePickerState) }
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
            Column(modifier = Modifier.width(64.dp).height(64.dp).clickable { onHomeClick() }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Icon(Icons.Default.Home, contentDescription = stringResource(id = R.string.nav_inicio), tint = iconColor, modifier = Modifier.size(26.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text(stringResource(id = R.string.nav_inicio), color = iconColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            Column(modifier = Modifier.width(64.dp).height(64.dp).clickable { onMatchesClick() }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text("⚽", fontSize = 24.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(stringResource(id = R.string.nav_partidos), color = iconColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
            Box(modifier = Modifier.width(76.dp).height(66.dp).clip(RoundedCornerShape(22.dp)).background(selectedBg), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.nav_crear), tint = selectedIcon, modifier = Modifier.size(26.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(stringResource(id = R.string.nav_crear), color = selectedIcon, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
            Column(modifier = Modifier.width(64.dp).height(64.dp).clickable { onProfileClick() }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Icon(Icons.Default.Person, contentDescription = stringResource(id = R.string.nav_perfil), tint = iconColor, modifier = Modifier.size(26.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text(stringResource(id = R.string.nav_perfil), color = iconColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}