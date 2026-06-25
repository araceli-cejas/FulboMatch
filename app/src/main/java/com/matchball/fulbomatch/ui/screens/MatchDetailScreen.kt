package com.matchball.fulbomatch.ui.screens

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.matchball.fulbomatch.data.model.Partido
import com.matchball.fulbomatch.data.model.UserProfile
import com.matchball.fulbomatch.ui.components.MoonIconButton
import com.matchball.fulbomatch.ui.partido.PartidoUiState
import com.matchball.fulbomatch.ui.partido.PartidoViewModel
import com.matchball.fulbomatch.ui.theme.GreenPrimary
import com.matchball.fulbomatch.ui.theme.White

private data class MatchDetailColors(
    val background: Color, val card: Color, val innerCard: Color, val textPrimary: Color, val textSecondary: Color,
    val textMuted: Color, val divider: Color, val headerIcon: Color, val accent: Color, val accentText: Color,
    val chipBackground: Color, val chipText: Color, val playerBackground: Color, val playerInactiveBorder: Color,
    val bottomBar: Color, val danger: Color
)

private fun matchDetailColors(isDarkMode: Boolean): MatchDetailColors {
    return if (isDarkMode) {
        MatchDetailColors(
            background = Color(0xFF111111), card = Color(0xFF1E1E1E), innerCard = Color(0xFF2A2A2A),
            textPrimary = Color(0xFFF2F2F2), textSecondary = Color(0xFFBDBDBD), textMuted = Color(0xFF9E9E9E),
            divider = Color(0xFF333333), headerIcon = Color(0xFFC9D1C9), accent = Color(0xFF9EF49B),
            accentText = Color(0xFF111111), chipBackground = Color(0xFF176B2A), chipText = Color(0xFFBDE8B9),
            playerBackground = Color(0xFF2E2E2E), playerInactiveBorder = Color(0xFF6A6A6A),
            bottomBar = Color(0xFF1A1A1A), danger = Color(0xFFFF6B6B)
        )
    } else {
        MatchDetailColors(
            background = Color(0xFFFAF9F8), card = Color(0xFFF1EFEF), innerCard = Color(0xFFE7E4E4),
            textPrimary = Color(0xFF202020), textSecondary = Color(0xFF4F574C), textMuted = Color(0xFF6F6F6F),
            divider = Color(0xFFE0E0E0), headerIcon = GreenPrimary, accent = GreenPrimary,
            accentText = White, chipBackground = GreenPrimary, chipText = Color(0xFFBDE8B9),
            playerBackground = Color(0xFFE9E7E7), playerInactiveBorder = Color(0xFFBFC6BC),
            bottomBar = Color(0xFFFAF9F8), danger = Color(0xFFD32F2F)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDetailScreen(
    matchId: String,
    onBackClick: () -> Unit,
    onJoinClick: () -> Unit = {},
    onLeaveClick: () -> Unit = {},
    onEditMatchClick: (String) -> Unit = {},
    onStatisticsClick: (String) -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {},
    viewModel: PartidoViewModel = viewModel()
) {
    val partidos by viewModel.partidos.collectAsState()
    val partido = partidos.find { it.id == matchId }
    val currentUserId = viewModel.currentUserId
    val jugadoresProfiles by viewModel.jugadoresConfirmados.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    var pendingAction by remember { mutableStateOf<String?>(null) }
    val colors = matchDetailColors(isDarkMode)
    var showFinishDialog by remember { mutableStateOf(false) }
    var golesLocalInput by remember { mutableStateOf("") }
    var golesVisitanteInput by remember { mutableStateOf("") }
    var duracionInput by remember { mutableStateOf("") }
    var amarillasInput by remember { mutableStateOf("") }
    var rojasInput by remember { mutableStateOf("") }

    LaunchedEffect(partido?.jugadoresConfirmados) {
        partido?.jugadoresConfirmados?.let { viewModel.cargarJugadoresConfirmados(it) }
    }

    LaunchedEffect(uiState) {
        if (uiState is PartidoUiState.Success) {
            if (pendingAction == "FINISH") {
                onStatisticsClick(matchId)
            }
            pendingAction = null
        }
    }
    if (showFinishDialog) {
        AlertDialog(
            onDismissRequest = { showFinishDialog = false },
            title = {
                Text(
                    "Cargar resultado",
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) Color(0xFFEDEDED) else Color(0xFF003311)
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Completá los datos del partido finalizado.",
                        color = if (isDarkMode) Color(0xFFBDBDBD) else Color(0xFF4F574C)
                    )
                    Spacer(Modifier.height(2.dp))

                    // Fila: Goles A y Goles B
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = golesLocalInput,
                            onValueChange = {
                                if (it.length <= 2 && it.all { c -> c.isDigit() })
                                    golesLocalInput = it
                            },
                            label = { Text("Goles A") },
                            singleLine = true,
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = golesVisitanteInput,
                            onValueChange = {
                                if (it.length <= 2 && it.all { c -> c.isDigit() })
                                    golesVisitanteInput = it
                            },
                            label = { Text("Goles B") },
                            singleLine = true,
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Duración
                    OutlinedTextField(
                        value = duracionInput,
                        onValueChange = {
                            if (it.length <= 3 && it.all { c -> c.isDigit() })
                                duracionInput = it
                        },
                        label = { Text("Duración (minutos)") },
                        placeholder = { Text("Ej: 60") },
                        singleLine = true,
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Fila: Tarjetas
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = amarillasInput,
                            onValueChange = {
                                if (it.length <= 2 && it.all { c -> c.isDigit() })
                                    amarillasInput = it
                            },
                            label = { Text("🟡 Amarillas") },
                            singleLine = true,
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = rojasInput,
                            onValueChange = {
                                if (it.length <= 2 && it.all { c -> c.isDigit() })
                                    rojasInput = it
                            },
                            label = { Text("🔴 Rojas") },
                            singleLine = true,
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val local = golesLocalInput.toIntOrNull() ?: 0
                        val visitante = golesVisitanteInput.toIntOrNull() ?: 0
                        val dur = duracionInput.toIntOrNull() ?: 0
                        val amarillas = amarillasInput.toIntOrNull() ?: 0
                        val rojas = rojasInput.toIntOrNull() ?: 0
                        showFinishDialog = false
                        pendingAction = "FINISH"
                        viewModel.finalizarPartido(
                            matchId, local, visitante, dur, amarillas, rojas
                        )
                    },
                    enabled = golesLocalInput.isNotEmpty()
                            && golesVisitanteInput.isNotEmpty()
                            && duracionInput.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Text("Confirmar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showFinishDialog = false
                }) {
                    Text(
                        "Cancelar",
                        color = if (isDarkMode) Color(0xFF9EF49B) else GreenPrimary
                    )
                }
            }
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle partido", color = colors.textPrimary, fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = colors.headerIcon) } },
                actions = {
                    MoonIconButton(isDarkMode, colors.headerIcon, onToggleDarkMode)
                    IconButton(onClick = onNotificationsClick) { Icon(Icons.Default.Notifications, null, tint = colors.headerIcon) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.background)
            )
        },
        bottomBar = {
            partido?.let { p ->
                BottomMatchActionButton(
                    isUserJoined = p.jugadoresConfirmados.contains(currentUserId),
                    isOrganizer = p.creadorId == currentUserId,
                    isFinished = p.status == "FINALIZADO",
                    colors = colors,
                    onJoinClick = { pendingAction = "JOIN"; viewModel.sumarseAPartido(matchId) },
                    onLeaveClick = { pendingAction = "LEAVE"; viewModel.bajarseDePartido(matchId) },
                    onEditClick = { onEditMatchClick(matchId) },
                    onFinishClick = {
                        golesLocalInput = ""
                        golesVisitanteInput = ""
                        showFinishDialog = true
                    },
                    onStatsClick = { onStatisticsClick(matchId) }
                )
            }
        },
        containerColor = colors.background
    ) { padding ->
        partido?.let { p ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 18.dp)
            ) {
                MatchHeroCard(p, p.jugadoresConfirmados.contains(currentUserId), colors)
                Spacer(modifier = Modifier.height(20.dp))
                OrganizerCard(jugadoresProfiles.find { it.id == p.creadorId }, colors, partidos)
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SurfaceCard(modifier = Modifier.weight(1f), title = "SUPERFICIE", value = p.superficie, icon = { SurfaceGrassIcon(Modifier.size(24.dp), colors.accent) }, colors = colors)
                    SurfaceCard(modifier = Modifier.weight(1f), title = "NIVEL", value = p.nivel, icon = { Text("🏆", fontSize = 18.sp) }, colors = colors)
                }
                Spacer(modifier = Modifier.height(20.dp))
                DetailSectionCard(colors = colors) {
                    Text("Reglas", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(p.descripcion.ifBlank { "Sin reglas adicionales." }, color = colors.textSecondary)
                }
                Spacer(modifier = Modifier.height(20.dp))
                ConfirmedPlayersCard(p, jugadoresProfiles, p.creadorId, colors)
                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }
}

@Composable
private fun MatchHeroCard(match: Partido, isUserJoined: Boolean, colors: MatchDetailColors) {
    val isFinished = match.status == "FINALIZADO"
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(colors.card)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.align(Alignment.TopEnd).offset(x = 55.dp, y = (-70).dp).size(170.dp).clip(CircleShape).background(colors.accent.copy(alpha = 0.16f)))
            Column(modifier = Modifier.padding(24.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = RoundedCornerShape(20.dp), color = if (isFinished) Color.LightGray else colors.chipBackground) {
                        Text(match.status, color = if (isFinished) Color.DarkGray else colors.chipText, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp))
                    }
                    if (isUserJoined && !isFinished) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(shape = RoundedCornerShape(20.dp), color = colors.innerCard) {
                            Text("ANOTADO", color = colors.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text("$${match.precio} / jug", color = colors.accent, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }
                Spacer(Modifier.height(18.dp))
                Text(match.titulo, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Place, null, tint = colors.textSecondary, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(match.lugar, fontSize = 15.sp, color = colors.textSecondary)
                }
                Spacer(Modifier.height(24.dp))
                if (isFinished) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        DetailSmallBox(Modifier.weight(0.8f), colors, { Icon(Icons.Default.DateRange, null, tint = colors.accent, modifier = Modifier.size(24.dp)) }, match.fecha)
                        Surface(modifier = Modifier.weight(1.2f).height(68.dp), shape = RoundedCornerShape(8.dp), color = colors.innerCard) {
                            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                Text("RESULTADO", fontSize = 10.sp, color = colors.textSecondary, fontWeight = FontWeight.Bold)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(match.golesLocal.toString(), fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = colors.accent)
                                    Text(" - ", fontSize = 20.sp, color = colors.textSecondary)
                                    Text(match.golesVisitante.toString(), fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF4D8DFF))
                                }
                            }
                        }
                    }
                } else {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        DetailSmallBox(Modifier.weight(1f), colors, { Icon(Icons.Default.DateRange, null, tint = colors.accent, modifier = Modifier.size(26.dp)) }, match.fecha)
                        DetailSmallBox(Modifier.weight(1f), colors, { Icon(Icons.Default.AccessTime, null, tint = colors.accent, modifier = Modifier.size(26.dp)) }, "${match.hora} hs")
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailSmallBox(modifier: Modifier, colors: MatchDetailColors, icon: @Composable () -> Unit, text: String) {
    Surface(modifier = modifier.height(68.dp), shape = RoundedCornerShape(8.dp), color = colors.innerCard) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            icon(); Spacer(Modifier.height(4.dp))
            Text(text, fontSize = 15.sp, color = colors.textPrimary, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SurfaceCard(modifier: Modifier, title: String, value: String, icon: @Composable () -> Unit, colors: MatchDetailColors) {
    Card(modifier = modifier.height(110.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(colors.card)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
            icon(); Spacer(Modifier.height(8.dp))
            Text(title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = colors.textMuted)
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
        }
    }
}

@Composable
private fun OrganizerCard(profile: UserProfile?, colors: MatchDetailColors, allMatches: List<Partido>) {
    val count = if (profile != null) allMatches.count { it.creadorId == profile.id || it.jugadoresConfirmados.contains(profile.id) } else 0
    DetailSectionCard(colors) {
        Text("Organizador", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
        Spacer(Modifier.height(12.dp)); HorizontalDivider(color = colors.divider); Spacer(Modifier.height(14.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(52.dp).clip(CircleShape).background(colors.playerBackground).border(2.dp, colors.accent, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, tint = colors.accent, modifier = Modifier.size(30.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column {
                Text(profile?.nombre ?: "Cargando...", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("★", color = colors.accent, fontSize = 16.sp)
                    Spacer(Modifier.width(4.dp))
                    Text("4.8 ($count partidos)", color = colors.textSecondary, fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
private fun ConfirmedPlayersCard(match: Partido, realPlayers: List<UserProfile>, organizerId: String?, colors: MatchDetailColors) {
    DetailSectionCard(colors) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Groups, null, tint = colors.textPrimary, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(8.dp))
            Text("Jugadores", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary, modifier = Modifier.weight(1f))
            Surface(shape = RoundedCornerShape(18.dp), color = colors.innerCard) {
                Text("${realPlayers.size}/${match.maxJugadores} Confirmados", color = colors.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp))
            }
        }
        Spacer(Modifier.height(22.dp))
        val slotsPerRow = 5
        val totalRows = (match.maxJugadores + slotsPerRow - 1) / slotsPerRow
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            for (r in 0 until totalRows) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    for (c in 0 until slotsPerRow) {
                        val i = (r * slotsPerRow) + c
                        if (i < match.maxJugadores) {
                            if (i < realPlayers.size) {
                                val p = realPlayers[i]
                                PlayerSlot(p.nombre.split(" ").first(), true, colors, p.id == organizerId, null, p.photoBase64)
                            } else PlayerSlot("Libre", false, colors)
                        } else Spacer(Modifier.width(56.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerSlot(name: String, active: Boolean, colors: MatchDetailColors, organizer: Boolean = false, letter: String? = null, photoBase64: String = "") {
    val bitmap = remember(photoBase64) {
        if (photoBase64.isNotEmpty()) {
            try {
                val bytes = Base64.decode(photoBase64, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            } catch (e: Exception) { null }
        } else null
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(56.dp)) {
        Box(Modifier.size(42.dp).clip(CircleShape).background(if (active) colors.playerBackground else Color.Transparent).then(if (organizer) Modifier.border(2.dp, colors.accent, CircleShape) else if (!active) Modifier.border(1.dp, colors.playerInactiveBorder, CircleShape) else Modifier), contentAlignment = Alignment.Center) {
            if (bitmap != null) Image(bitmap.asImageBitmap(), null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            else if (organizer) Icon(Icons.Default.Person, null, tint = colors.accent, modifier = Modifier.size(24.dp))
            else if (active) Icon(Icons.Default.Person, null, tint = colors.textMuted, modifier = Modifier.size(20.dp))
            else Text("+", fontSize = 20.sp, color = colors.playerInactiveBorder)
            if (organizer) Text("★", color = colors.accent, fontSize = 12.sp, modifier = Modifier.align(Alignment.BottomEnd).background(colors.playerBackground, CircleShape))
        }
        Spacer(Modifier.height(6.dp))
        Text(name, fontSize = 12.sp, fontWeight = if (organizer) FontWeight.Bold else FontWeight.Normal, color = if (active) (if (organizer) colors.accent else colors.textSecondary) else colors.playerInactiveBorder, maxLines = 1)
    }
}

@Composable
private fun DetailSectionCard(colors: MatchDetailColors, content: @Composable ColumnScope.() -> Unit) {
    Card(Modifier.fillMaxWidth(), RoundedCornerShape(12.dp), CardDefaults.cardColors(colors.card)) {
        Column(Modifier.padding(24.dp), content = content)
    }
}

@Composable
private fun DetailInfoRow(colors: MatchDetailColors, icon: @Composable () -> Unit, title: String, desc: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(Modifier.width(34.dp), Alignment.TopStart) { icon() }
        Column {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
            Text(desc, fontSize = 15.sp, color = colors.textSecondary, lineHeight = 20.sp)
        }
    }
}

@Composable
private fun BottomMatchActionButton(isUserJoined: Boolean, isOrganizer: Boolean, isFinished: Boolean, colors: MatchDetailColors, onJoinClick: () -> Unit, onLeaveClick: () -> Unit, onEditClick: () -> Unit, onFinishClick: () -> Unit, onStatsClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), color = colors.bottomBar, shadowElevation = 8.dp) {
        Column(Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            if (isFinished) {
                Button(onStatsClick, Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(24.dp), colors = ButtonDefaults.buttonColors(colors.accent, colors.accentText)) {
                    Text("Ver estadísticas", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            } else if (isOrganizer) {
                Button(onEditClick, Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(24.dp), colors = ButtonDefaults.buttonColors(colors.accent, colors.accentText)) {
                    Text("Editar partido", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(12.dp))
                OutlinedButton(onFinishClick, Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(24.dp), border = BorderStroke(1.dp, colors.accent), colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.accent)) {
                    Text("Finalizar partido", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(8.dp))
                Text("Solo el organizador puede modificar o finalizar este partido.", fontSize = 13.sp, color = colors.textMuted, textAlign = TextAlign.Center)
            } else if (isUserJoined) {
                OutlinedButton(onLeaveClick, Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(24.dp), border = BorderStroke(1.dp, colors.danger), colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.danger)) {
                    Text("Bajarme del partido", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Button(onJoinClick, Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(24.dp), colors = ButtonDefaults.buttonColors(colors.accent, colors.accentText)) {
                    Text("Sumarme al partido", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp)); Text("⚽", fontSize = 20.sp)
                }
            }
        }
    }
}

@Composable
private fun SurfaceGrassIcon(modifier: Modifier = Modifier, color: Color = GreenPrimary) {
    Canvas(modifier) {
        val sw = 2.4.dp.toPx()
        drawLine(color, Offset(size.width * 0.15f, size.height * 0.78f), Offset(size.width * 0.85f, size.height * 0.78f), sw, StrokeCap.Round)
        val p1 = Path().apply { moveTo(size.width * 0.50f, size.height * 0.78f); quadraticTo(size.width * 0.46f, size.height * 0.45f, size.width * 0.34f, size.height * 0.28f) }
        drawPath(p1, color, style = Stroke(sw, cap = StrokeCap.Round))
        val p2 = Path().apply { moveTo(size.width * 0.36f, size.height * 0.78f); quadraticTo(size.width * 0.26f, size.height * 0.56f, size.width * 0.14f, size.height * 0.50f) }
        drawPath(p2, color, style = Stroke(sw, cap = StrokeCap.Round))
        val p3 = Path().apply { moveTo(size.width * 0.62f, size.height * 0.78f); quadraticTo(size.width * 0.72f, size.height * 0.56f, size.width * 0.86f, size.height * 0.48f) }
        drawPath(p3, color, style = Stroke(sw, cap = StrokeCap.Round))
    }
}
