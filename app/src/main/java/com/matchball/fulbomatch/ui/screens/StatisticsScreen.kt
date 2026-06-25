package com.matchball.fulbomatch.ui.screens

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.matchball.fulbomatch.data.model.Partido
import com.matchball.fulbomatch.data.model.UserProfile
import com.matchball.fulbomatch.ui.components.MoonIconButton
import com.matchball.fulbomatch.ui.partido.PartidoViewModel
import com.matchball.fulbomatch.ui.theme.GreenPrimary

private data class StatisticsColors(
    val background: Color, val cardBackground: Color, val textPrimary: Color, val textSecondary: Color,
    val textMuted: Color, val headerIcon: Color, val icon: Color, val accent: Color, val teamBlue: Color,
    val divider: Color, val border: Color, val bottomBarBackground: Color, val bottomIcon: Color
)

private fun statisticsColors(isDarkMode: Boolean): StatisticsColors {
    return if (isDarkMode) {
        StatisticsColors(
            background = Color(0xFF111111), cardBackground = Color(0xFF1A1A1A), textPrimary = Color(0xFFEDEDED),
            textSecondary = Color(0xFFBDBDBD), textMuted = Color(0xFF9E9E9E), headerIcon = Color(0xFFC9D1C9),
            icon = Color(0xFFC9D1C9), accent = Color(0xFF9EF49B), teamBlue = Color(0xFF4D8DFF),
            divider = Color(0xFF2A2A2A), border = Color(0xFF555555), bottomBarBackground = Color(0xFF151515), bottomIcon = Color(0xFFC9D1C9)
        )
    } else {
        StatisticsColors(
            background = Color(0xFFF6F8FA), cardBackground = Color.White, textPrimary = Color(0xFF003311),
            textSecondary = Color(0xFF4F574C), textMuted = Color(0xFF6D6D6D), headerIcon = GreenPrimary,
            icon = Color(0xFF6D6D6D), accent = GreenPrimary, teamBlue = Color(0xFF004AAD),
            divider = Color(0xFFE0E0E0), border = Color(0xFFB9B9B9), bottomBarBackground = Color(0xFFF3F1F1), bottomIcon = Color(0xFF4F574C)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    matchId: String,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onMatchesClick: () -> Unit,
    onCreateClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit = {},
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {},
    viewModel: PartidoViewModel = viewModel()
) {
    val partidos by viewModel.partidos.collectAsState()
    val partido = partidos.find { it.id == matchId }
    val jugadoresProfiles by viewModel.jugadoresConfirmados.collectAsState()
    val colors = statisticsColors(isDarkMode)

    val detail = partido?.toFinishedDetail(jugadoresProfiles) ?: getFinishedMatchDetail(matchId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadísticas", color = if (isDarkMode) colors.textPrimary else Color(0xFF003311), fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = colors.headerIcon) } },
                actions = {
                    MoonIconButton(isDarkMode, colors.headerIcon, onToggleDarkMode)
                    IconButton(onClick = onNotificationsClick) { Icon(Icons.Default.Notifications, null, tint = colors.headerIcon) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.background)
            )
        },
        bottomBar = { StatisticsBottomBar(colors, onHomeClick, onMatchesClick, onCreateClick, onProfileClick) },
        containerColor = colors.background
    ) { padding ->
        if (detail == null) {
            Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                CircularProgressIndicator(color = GreenPrimary)
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(18.dp))
                ScoreCard(detail, colors)
                Spacer(Modifier.height(28.dp))
                Text("Métricas Clave", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = if (isDarkMode) colors.textPrimary else Color(0xFF003311))
                Spacer(Modifier.height(14.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    MetricCard(Modifier.weight(1f), colors, { Text("⚽", fontSize = 24.sp) }, detail.totalGoals.toString(), "Goles Totales")
                    MetricCard(Modifier.weight(1f), colors, { Icon(Icons.Default.AccessTime, null, tint = colors.icon) }, "60 min", "Duración")
                }
                Spacer(Modifier.height(14.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    MetricCard(Modifier.weight(1f), colors, { Box(Modifier.size(20.dp, 28.dp).background(Color(0xFFE0A800), RoundedCornerShape(2.dp))) }, "2", "Tarjetas Amarillas")
                    MetricCard(Modifier.weight(1f), colors, { Box(Modifier.size(20.dp, 28.dp).background(Color(0xFFFF4D4D), RoundedCornerShape(2.dp))) }, "0", "Tarjetas Rojas")
                }
                Spacer(Modifier.height(34.dp))

            }
        }
    }
}

@Composable
private fun ScoreCard(match: FinishedMatchDetail, colors: StatisticsColors) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(colors.cardBackground), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("FINALIZADO", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = colors.textMuted, letterSpacing = 1.sp)
            Text(match.statistics.dateTimeAndPlace, fontSize = 13.sp, color = colors.textSecondary, textAlign = TextAlign.Center)
            Spacer(Modifier.height(28.dp))
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                TeamColumn("Equipo A", Color(0xFF007A33), colors)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(match.resultHome, fontSize = 52.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF007A33))
                    Text(" - ", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = colors.textMuted, modifier = Modifier.padding(horizontal = 8.dp))
                    Text(match.resultAway, fontSize = 52.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF004AAD))
                }
                TeamColumn("Equipo B", Color(0xFF004AAD), colors)
            }
        }
    }
}

@Composable
private fun TeamColumn(name: String, color: Color, colors: StatisticsColors) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(56.dp).clip(CircleShape).background(color), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.SportsSoccer, null, tint = Color.White, modifier = Modifier.size(28.dp))
        }
        Spacer(Modifier.height(10.dp))
        Text(name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
    }
}

@Composable
private fun MetricCard(modifier: Modifier, colors: StatisticsColors, icon: @Composable () -> Unit, value: String, label: String) {
    Card(modifier = modifier.height(120.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(colors.cardBackground), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            icon(); Spacer(Modifier.height(8.dp))
            Text(value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
            Text(label, fontSize = 11.sp, color = colors.textSecondary, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun ScorerCard(scorer: FinishedMatchScorer, colors: StatisticsColors) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(colors.cardBackground), elevation = CardDefaults.cardElevation(1.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(48.dp).clip(CircleShape).background(Color(0xFFE0E0E0)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, tint = Color.Gray)
            }
            Spacer(Modifier.width(16.dp))
            Text(scorer.name, modifier = Modifier.weight(1f), fontSize = 16.sp, fontWeight = FontWeight.Medium, color = colors.textPrimary)
            Surface(shape = RoundedCornerShape(8.dp), color = colors.background, border = BorderStroke(1.dp, colors.border)) {
                Text("${scorer.goals} Goles", modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = colors.textPrimary)
            }
        }
    }
}

@Composable
private fun StatisticsBottomBar(colors: StatisticsColors, onHomeClick: () -> Unit, onMatchesClick: () -> Unit, onCreateClick: () -> Unit, onProfileClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), color = colors.bottomBarBackground, shadowElevation = 8.dp) {
        Row(modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 22.dp, vertical = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            BottomItem(Icons.Default.Home, "Inicio", false, colors, onHomeClick)
            BottomItem("⚽", "Partidos", true, colors, onMatchesClick)
            BottomItem(Icons.Default.Add, "Crear", false, colors, onCreateClick)
            BottomItem(Icons.Default.Person, "Perfil", false, colors, onProfileClick)
        }
    }
}

@Composable
private fun BottomItem(icon: Any, label: String, selected: Boolean, colors: StatisticsColors, onClick: () -> Unit) {
    Column(modifier = Modifier.width(72.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(onClick = onClick, shape = RoundedCornerShape(22.dp), color = if (selected) colors.accent else Color.Transparent, modifier = Modifier.height(44.dp).width(if (selected) 64.dp else 44.dp)) {
            Box(contentAlignment = Alignment.Center) {
                if (icon is ImageVector) Icon(icon, null, tint = if (selected) Color.Black else colors.bottomIcon)
                else Text(icon.toString(), fontSize = 20.sp)
            }
        }
        Text(label, fontSize = 11.sp, color = colors.bottomIcon)
    }
}

private fun Partido.toFinishedDetail(players: List<UserProfile>): FinishedMatchDetail {
    val scorers = players.take(2).mapIndexed { i, p -> FinishedMatchScorer(p.nombre, if (i == 0) "Equipo A" else "Equipo B", if (i == 0) 3 else 2) }
    return FinishedMatchDetail(
        id = id, title = titulo, location = lugar, date = fecha, time = hora,
        resultHome = golesLocal.toString(), resultAway = golesVisitante.toString(),
        organizerName = "Organizador", rating = "5.0", ratingDescription = "(1 partido)",
        surface = superficie, level = nivel, players = "${jugadoresConfirmados.size}/$maxJugadores",
        statistics = FinishedMatchStatistics("${fecha}, $hora - $lugar", "Equipo A", "Equipo B", 60, 2, 0, scorers)
    )
}
