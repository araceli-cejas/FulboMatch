package com.matchball.fulbomatch.ui.screens

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import com.matchball.fulbomatch.ui.partido.PartidoViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matchball.fulbomatch.R
import com.matchball.fulbomatch.ui.components.MoonIconButton
import com.matchball.fulbomatch.ui.theme.GreenPrimary

private enum class MatchFilter(val label: String) {
    TODOS("Todos"),
    ME_SUME("Me sumé"),
    ORGANIZO("Organicé")
}

data class UserMatch(
    val id: String, val title: String, val location: String, val day: String,
    val month: String, val time: String, val players: String, val status: String,
    val isUserJoined: Boolean, val isOrganizer: Boolean
)

data class PastMatch(
    val id: String, val title: String, val location: String, val day: String,
    val month: String, val time: String, val result: String,
    val isUserJoined: Boolean, val isOrganizer: Boolean, val status: String = "FINALIZADO"
)

@Composable
fun MatchesScreen(
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {},
    onHomeClick: () -> Unit,
    onCreateMatchClick: () -> Unit,
    onProfileClick: () -> Unit,
    onMatchClick: (String) -> Unit,
    onRequestsClick: () -> Unit = {},
    viewModel: PartidoViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf("Próximos") }
    var selectedFilter by remember { mutableStateOf(MatchFilter.TODOS) }

    val context = LocalContext.current
    val isOnline = isOnline(context)
    val colors = matchesColors(isDarkMode)

    val partidosFirebase by viewModel.partidos.collectAsState()
    val currentUserId = viewModel.currentUserId

    LaunchedEffect(Unit) { viewModel.loadPartidos() }

    // Filtrar solo partidos del usuario actual
    val misPartidos = partidosFirebase.filter { p ->
        p.creadorId == currentUserId || p.jugadoresConfirmados.contains(currentUserId)
    }

    // Mapa a Próximos (Solo PENDIENTE o ABIERTO)
    val realUserMatches = misPartidos.filter { it.status == "PENDIENTE" || it.status == "ABIERTO" }.map { p ->
        val fechaParts = p.fecha.split("/")
        UserMatch(
            id = p.id, title = p.titulo, location = p.lugar,
            day = fechaParts.getOrNull(0) ?: "00", month = fechaParts.getOrNull(1) ?: "MES",
            time = "${p.hora} hs", players = "${p.jugadoresConfirmados.size}/${p.maxJugadores} jugadores",
            status = if (p.jugadoresConfirmados.size >= p.maxJugadores) "CONFIRMADO" else "PENDIENTE",
            isUserJoined = p.jugadoresConfirmados.contains(currentUserId),
            isOrganizer = p.creadorId == currentUserId
        )
    }

    // Mapa a Pasados (FINALIZADO o CANCELADO)
    val realPastMatches = misPartidos.filter { it.status == "FINALIZADO" || it.status == "CANCELADO" }.map { p ->
        val fechaParts = p.fecha.split("/")
        PastMatch(
            id = p.id, title = p.titulo, location = p.lugar,
            day = fechaParts.getOrNull(0) ?: "00", month = fechaParts.getOrNull(1) ?: "MES",
            time = "${p.hora} hs", result = p.status,
            isUserJoined = p.jugadoresConfirmados.contains(currentUserId),
            isOrganizer = p.creadorId == currentUserId,
            status = p.status
        )
    }


    Scaffold(
        bottomBar = { MatchesBottomBar(colors, onHomeClick, onCreateMatchClick, onProfileClick) },
        containerColor = colors.background
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).background(colors.background)
                .verticalScroll(rememberScrollState()).padding(horizontal = 22.dp, vertical = 12.dp)
        ) {
            MatchesHeader(colors, isDarkMode, onToggleDarkMode, onRequestsClick)
            Text("Partidos", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
            MatchesTabs(selectedTab, colors) { selectedTab = it }
            MatchesFilterChips(selectedFilter, colors) { selectedFilter = it }

            if (!isOnline && misPartidos.isNotEmpty()) OfflineWarningBanner()

            if (selectedTab == "Próximos") {
                val visible = when (selectedFilter) {
                    MatchFilter.TODOS -> realUserMatches
                    MatchFilter.ME_SUME -> realUserMatches.filter { it.isUserJoined && !it.isOrganizer }
                    MatchFilter.ORGANIZO -> realUserMatches.filter { it.isOrganizer }
                }
                if (visible.isEmpty()) EmptyFilteredMatches(selectedFilter, colors)
                else visible.forEach { UserMatchCard(it, colors) { onMatchClick(it.id) }; Spacer(modifier = Modifier.height(12.dp)) }
            } else {
                val visible = when (selectedFilter) {
                    MatchFilter.TODOS -> realPastMatches
                    MatchFilter.ME_SUME -> realPastMatches.filter { it.isUserJoined && !it.isOrganizer }
                    MatchFilter.ORGANIZO -> realPastMatches.filter { it.isOrganizer }
                }
                if (visible.isEmpty()) EmptyFilteredMatches(selectedFilter, colors)
                else visible.forEach { PastMatchCard(it, colors) { onMatchClick(it.id) }; Spacer(modifier = Modifier.height(12.dp)) }
            }
        }
    }
}

// [MANTENER MatchesHeader, MatchesTabs, Chips, UserMatchCard, PastMatchCard, LocationRow, DateBox, StatusChip, BottomBar y Helpers IGUAL QUE ANTES]
@Composable
private fun MatchesHeader(
    colors: MatchesColors,
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
private fun MatchesTabs(
    selectedTab: String,
    colors: MatchesColors,
    onTabSelected: (String) -> Unit
) {
    val tabs = listOf("Próximos", "Pasados")

    Column {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEach { tab ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onTabSelected(tab) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = tab,
                        fontSize = 14.sp,
                        fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTab == tab) colors.accent else colors.textSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(
                                if (selectedTab == tab) colors.accent else Color.Transparent
                            )
                    )
                }
            }
        }

        HorizontalDivider(
            color = colors.divider,
            thickness = 1.dp
        )
    }
}

@Composable
private fun MatchesFilterChips(
    selectedFilter: MatchFilter,
    colors: MatchesColors,
    onFilterSelected: (MatchFilter) -> Unit
) {
    Spacer(modifier = Modifier.height(12.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        MatchFilter.values().forEach { filter ->
            MatchFilterChip(
                filter = filter,
                selected = selectedFilter == filter,
                colors = colors,
                onClick = { onFilterSelected(filter) }
            )
        }
    }
}

@Composable
private fun MatchFilterChip(
    filter: MatchFilter,
    selected: Boolean,
    colors: MatchesColors,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (selected) colors.selectedChipBackground else colors.cardBackground,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) colors.selectedChipBorder else colors.border
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MatchFilterIcon(
                filter = filter,
                selected = selected,
                colors = colors
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = filter.label,
                fontSize = 14.sp,
                fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
                color = if (selected) colors.selectedChipText else colors.textSecondary
            )
        }
    }
}

@Composable
private fun MatchFilterIcon(
    filter: MatchFilter,
    selected: Boolean,
    colors: MatchesColors
) {
    when (filter) {
        MatchFilter.TODOS -> {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(if (selected) colors.selectedChipText else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✓",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selected) Color.White else colors.textSecondary
                )
            }
        }

        MatchFilter.ME_SUME -> {
            Icon(
                imageVector = Icons.Default.Groups,
                contentDescription = "Me sumé",
                tint = if (selected) colors.selectedChipText else colors.textSecondary,
                modifier = Modifier.size(18.dp)
            )
        }

        MatchFilter.ORGANIZO -> {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Organicé",
                tint = if (selected) colors.selectedChipText else colors.textSecondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun EmptyFilteredMatches(
    selectedFilter: MatchFilter,
    colors: MatchesColors
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = when (selectedFilter) {
                MatchFilter.TODOS -> "Todavía no tenés partidos pasados."
                MatchFilter.ME_SUME -> "Todavía no te sumaste a ningún partido."
                MatchFilter.ORGANIZO -> "Todavía no organizaste ningún partido."
            },
            fontSize = 15.sp,
            color = colors.textMuted
        )
    }
}

@Composable
private fun UserMatchCard(
    match: UserMatch,
    colors: MatchesColors,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = match.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.textPrimary,
                    modifier = Modifier.weight(1f)
                )

                StatusChip(status = match.status)
            }

            Spacer(modifier = Modifier.height(4.dp))

            LocationRow(
                location = match.location,
                colors = colors
            )

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(
                color = colors.divider,
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                DateBox(
                    day = match.day,
                    month = match.month,
                    colors = colors
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Hora",
                            tint = colors.icon,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = match.time,
                            fontSize = 14.sp,
                            color = colors.textPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = "Jugadores",
                            tint = colors.icon,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = match.players,
                            fontSize = 14.sp,
                            color = colors.textSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                color = colors.divider,
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Ver detalles >",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colors.accent,
                modifier = Modifier.clickable { onClick() }
            )
        }
    }
}

@Composable
private fun PastMatchCard(
    match: PastMatch,
    colors: MatchesColors,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = match.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.textPrimary,
                    modifier = Modifier.weight(1f)
                )

                FinishedStatusChip(status = match.status)
            }

            Spacer(modifier = Modifier.height(4.dp))

            LocationRow(
                location = match.location,
                colors = colors
            )

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(
                color = colors.divider,
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DateBox(
                    day = match.day,
                    month = match.month,
                    colors = colors
                )

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "Hora",
                                tint = colors.icon,
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = match.time,
                                fontSize = 14.sp,
                                color = colors.textPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = match.result,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.accent
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                color = colors.divider,
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Ver detalles >",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colors.accent,
                modifier = Modifier.clickable { onClick() }
            )
        }
    }
}

@Composable
private fun LocationRow(
    location: String,
    colors: MatchesColors
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Place,
            contentDescription = "Ubicación",
            tint = colors.icon,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = location,
            fontSize = 14.sp,
            color = colors.textSecondary
        )
    }
}

@Composable
private fun DateBox(
    day: String,
    month: String,
    colors: MatchesColors
) {
    Surface(
        modifier = Modifier.size(width = 62.dp, height = 64.dp),
        shape = RoundedCornerShape(6.dp),
        color = colors.dateBoxBackground
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = day,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                color = colors.accent
            )

            Text(
                text = month,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = colors.accent
            )
        }
    }
}

@Composable
private fun StatusChip(status: String) {
    val isConfirmed = status == "CONFIRMADO"
    val isCancelled = status == "CANCELADO"

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = when {
            isConfirmed -> Color(0xFFDDF8E6)
            isCancelled -> Color(0xFFFFD6D6)
            else -> Color(0xFFFFE7D2)
        }
    ) {
        Text(
            text = status,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = when {
                isConfirmed -> Color(0xFF008A35)
                isCancelled -> Color(0xFFC62828)
                else -> Color(0xFFC85D00)
            },
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
private fun FinishedStatusChip(status: String = "FINALIZADO") {
    val isCancelled = status == "CANCELADO"
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = if (isCancelled) Color(0xFFFFD6D6) else Color(0xFFE5E5E5)
    ) {
        Text(
            text = status,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (isCancelled) Color(0xFFC62828) else Color(0xFF7A7A7A),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
private fun MatchesBottomBar(
    colors: MatchesColors,
    onHomeClick: () -> Unit,
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
            UnselectedBottomItem(
                icon = Icons.Default.Home,
                label = "Inicio",
                colors = colors,
                onClick = onHomeClick
            )

            SelectedBottomItem(
                iconText = "⚽",
                label = "Partidos",
                onClick = { }
            )

            UnselectedBottomItem(
                icon = Icons.Default.Add,
                label = "Crear",
                colors = colors,
                onClick = onCreateMatchClick
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
    iconText: String,
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
            Text(
                text = iconText,
                fontSize = 24.sp,
                color = Color(0xFFBDE8B9)
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
    colors: MatchesColors,
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
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = colors.bottomIcon,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun OfflineWarningBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF3E0))
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CloudOff,
            contentDescription = "Sin conexión",
            tint = Color(0xFFEF6C00),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Modo sin conexión. Mostrando datos locales.",
            fontSize = 13.sp,
            color = Color(0xFFEF6C00),
            fontWeight = FontWeight.SemiBold
        )
    }
}

fun isOnline(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}

private data class MatchesColors(
    val background: Color,
    val cardBackground: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val icon: Color,
    val headerIcon: Color,
    val border: Color,
    val divider: Color,
    val accent: Color,
    val selectedChipBackground: Color,
    val selectedChipText: Color,
    val selectedChipBorder: Color,
    val dateBoxBackground: Color,
    val bottomBarBackground: Color,
    val bottomIcon: Color
)

private fun matchesColors(isDarkMode: Boolean): MatchesColors {
    return if (isDarkMode) {
        MatchesColors(
            background = Color(0xFF111111),
            cardBackground = Color(0xFF1A1A1A),
            textPrimary = Color(0xFFEDEDED),
            textSecondary = Color(0xFFBDBDBD),
            textMuted = Color(0xFF9E9E9E),
            icon = Color(0xFFC9D1C9),
            headerIcon = Color(0xFFC9D1C9),
            border = Color(0xFF3E463E),
            divider = Color(0xFF2A2A2A),
            accent = Color(0xFF9EF49B),
            selectedChipBackground = Color(0xFF9EF49B),
            selectedChipText = Color(0xFF111111),
            selectedChipBorder = Color(0xFF9EF49B),
            dateBoxBackground = Color(0xFF22302B),
            bottomBarBackground = Color(0xFF151515),
            bottomIcon = Color(0xFFC9D1C9)
        )
    } else {
        MatchesColors(
            background = Color(0xFFFAF9F8),
            cardBackground = Color(0xFFF7F5F5),
            textPrimary = Color(0xFF202020),
            textSecondary = Color(0xFF4F574C),
            textMuted = Color(0xFF8A8A8A),
            icon = Color(0xFF4F574C),
            headerIcon = GreenPrimary,
            border = Color(0xFFE5E2E2),
            divider = Color(0xFFE8E5E5),
            accent = GreenPrimary,
            selectedChipBackground = Color(0xFFE2F8E9),
            selectedChipText = GreenPrimary,
            selectedChipBorder = Color(0xFFB9EFC8),
            dateBoxBackground = Color(0xFFE5F0E4),
            bottomBarBackground = Color(0xFFF3F1F1),
            bottomIcon = Color(0xFF4F574C)
        )
    }
}