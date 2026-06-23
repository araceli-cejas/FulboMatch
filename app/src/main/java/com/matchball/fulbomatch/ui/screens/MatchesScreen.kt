package com.matchball.fulbomatch.ui.screens

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import com.matchball.fulbomatch.ui.screens.PartidoViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
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
    val id: String,
    val title: String,
    val location: String,
    val day: String,
    val month: String,
    val time: String,
    val players: String,
    val status: String,
    val isUserJoined: Boolean,
    val isOrganizer: Boolean
)

data class PastMatch(
    val id: String,
    val title: String,
    val location: String,
    val day: String,
    val month: String,
    val time: String,
    val result: String,
    val isUserJoined: Boolean,
    val isOrganizer: Boolean,
    val status: String = "FINALIZADO"
)

val userMatches = listOf(
    UserMatch(
        id = "1",
        title = "Fútbol 5 - La Canchita",
        location = "Palermo, CABA",
        day = "25",
        month = "OCT",
        time = "20:00 hs",
        players = "10/10 jugadores",
        status = "CONFIRMADO",
        isUserJoined = true,
        isOrganizer = false
    ),
    UserMatch(
        id = "2",
        title = "Fútbol 7 Mix",
        location = "Caballito, CABA",
        day = "26",
        month = "OCT",
        time = "18:30 hs",
        players = "12/14 jugadores",
        status = "PENDIENTE",
        isUserJoined = false,
        isOrganizer = true
    )
)

val pastMatches = listOf(
    PastMatch(
        id = "past_1",
        title = "Cancha El Clásico",
        location = "Palermo, CABA",
        day = "12",
        month = "OCT",
        time = "19:00 hs",
        result = "5 - 3",
        isUserJoined = true,
        isOrganizer = false
    ),
    PastMatch(
        id = "past_2",
        title = "La Canchita F5",
        location = "Belgrano, CABA",
        day = "05",
        month = "OCT",
        time = "20:30 hs",
        result = "2 - 2",
        isUserJoined = true,
        isOrganizer = false
    ),
    PastMatch(
        id = "past_3",
        title = "Fútbol City",
        location = "Vicente López, GBA",
        day = "28",
        month = "SEP",
        time = "18:00 hs",
        result = "1 - 4",
        isUserJoined = false,
        isOrganizer = true
    )
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
    viewModel: PartidoViewModel = viewModel() // <- 1. Inyectamos el ViewModel
) {
    var selectedTab by remember { mutableStateOf("Próximos") }
    var selectedFilter by remember { mutableStateOf(MatchFilter.TODOS) }

    val colors = matchesColors(isDarkMode)

    // --- FIREBASE INTEGRACIÓN ---
    // 2. Escuchamos los partidos de la base de datos
    val partidosFirebase by viewModel.partidos.collectAsState()
    val currentUserId = viewModel.currentUserId

    // 3. Forzamos la recarga al entrar a la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadPartidos()
    }

    // 4. Filtramos SOLO los partidos donde el usuario participa (es creador o está confirmado)
    val misPartidosFirebase = partidosFirebase.filter { partido ->
        partido.creadorId == currentUserId || partido.jugadoresConfirmados.contains(currentUserId)
    }

    // 5. Mapeamos a UserMatch (el formato que usa tu diseño)
    val realUserMatches = misPartidosFirebase.map { p ->
        val fechaParts = p.fecha.split("/")
        val isOrganizer = p.creadorId == currentUserId

        UserMatch(
            id = p.id,
            title = p.titulo,
            location = p.lugar,
            day = fechaParts.getOrNull(0) ?: "00",
            month = fechaParts.getOrNull(1) ?: "MES", // Podés armar una funcioncita para pasar el "10" a "OCT"
            time = "${p.hora} hs",
            players = "${p.jugadoresConfirmados.size}/${p.maxJugadores} jugadores",
            status = if (p.jugadoresConfirmados.size >= p.maxJugadores) "CONFIRMADO" else "PENDIENTE",
            isUserJoined = p.jugadoresConfirmados.contains(currentUserId),
            isOrganizer = isOrganizer
        )
    }

    Scaffold(
        bottomBar = {
            MatchesBottomBar(
                colors = colors,
                onHomeClick = onHomeClick,
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
                .padding(horizontal = 22.dp, vertical = 12.dp)
                .semantics { contentDescription = "Pantalla de partidos" }
        ) {
            MatchesHeader(
                colors = colors,
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
                onNotificationsClick = onRequestsClick
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Partidos",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(22.dp))

            MatchesTabs(
                selectedTab = selectedTab,
                colors = colors,
                onTabSelected = { selectedTab = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            MatchesFilterChips(
                selectedFilter = selectedFilter,
                colors = colors,
                onFilterSelected = { selectedFilter = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (selectedTab == "Próximos") {
                // 6. Usamos realUserMatches en vez de la lista falsa
                val visibleMatches = when (selectedFilter) {
                    MatchFilter.TODOS -> realUserMatches
                    MatchFilter.ME_SUME -> realUserMatches.filter { it.isUserJoined && !it.isOrganizer }
                    MatchFilter.ORGANIZO -> realUserMatches.filter { it.isOrganizer }
                }

                if (visibleMatches.isEmpty()) {
                    EmptyFilteredMatches(
                        selectedFilter = selectedFilter,
                        colors = colors
                    )
                } else {
                    visibleMatches.forEach { match ->
                        UserMatchCard(
                            match = match,
                            colors = colors,
                            onClick = { onMatchClick(match.id) }
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            } else {
                // Para los partidos pasados lo dejamos con el mock por ahora
                // ya que requiere lógica de comparar fechas para saber si ya terminó
                val visiblePastMatches = when (selectedFilter) {
                    MatchFilter.TODOS -> pastMatches
                    MatchFilter.ME_SUME -> pastMatches.filter { it.isUserJoined }
                    MatchFilter.ORGANIZO -> pastMatches.filter { it.isOrganizer }
                }

                if (visiblePastMatches.isEmpty()) {
                    EmptyFilteredMatches(
                        selectedFilter = selectedFilter,
                        colors = colors
                    )
                } else {
                    visiblePastMatches.forEach { match ->
                        PastMatchCard(
                            match = match,
                            colors = colors,
                            onClick = { onMatchClick(match.id) }
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

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
                MatchFilter.TODOS -> "Todavía no tenés partidos próximos."
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

                FinishedStatusChip()
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

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = if (isConfirmed) Color(0xFFDDF8E6) else Color(0xFFFFE7D2)
    ) {
        Text(
            text = status,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (isConfirmed) Color(0xFF008A35) else Color(0xFFC85D00),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Composable
private fun FinishedStatusChip() {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFE5E5E5)
    ) {
        Text(
            text = "FINALIZADO",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF7A7A7A),
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