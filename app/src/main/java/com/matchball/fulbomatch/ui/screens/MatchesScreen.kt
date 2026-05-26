package com.matchball.fulbomatch.ui.screens

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
import com.matchball.fulbomatch.ui.theme.GreenPrimary
import com.matchball.fulbomatch.ui.components.MoonIconButton

private enum class MatchFilter(val label: String) {
    TODOS("Todos"),
    ME_SUME("Me sumé"),
    ORGANIZO("Organizo")
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
    onHomeClick: () -> Unit,
    onCreateMatchClick: () -> Unit,
    onProfileClick: () -> Unit,
    onMatchClick: (String) -> Unit,
    onRequestsClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf("Próximos") }
    var selectedFilter by remember { mutableStateOf(MatchFilter.TODOS) }

    Scaffold(
        bottomBar = {
            MatchesBottomBar(
                onHomeClick = onHomeClick,
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
                .padding(horizontal = 22.dp, vertical = 12.dp)
                .semantics { contentDescription = "Pantalla de partidos" }
        ) {
            MatchesHeader(onNotificationsClick = onRequestsClick)

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Partidos",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF202020)
            )

            Spacer(modifier = Modifier.height(22.dp))

            MatchesTabs(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedTab == "Próximos") {
                MatchesFilterChips(
                    selectedFilter = selectedFilter,
                    onFilterSelected = { selectedFilter = it }
                )

                Spacer(modifier = Modifier.height(20.dp))

                val visibleMatches = when (selectedFilter) {
                    MatchFilter.TODOS -> userMatches
                    MatchFilter.ME_SUME -> userMatches.filter { it.isUserJoined }
                    MatchFilter.ORGANIZO -> userMatches.filter { it.isOrganizer }
                }

                if (visibleMatches.isEmpty()) {
                    EmptyFilteredMatches(selectedFilter = selectedFilter)
                } else {
                    visibleMatches.forEach { match ->
                        UserMatchCard(
                            match = match,
                            onClick = { onMatchClick(match.id) }
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            } else {
                MatchesFilterChips(
                    selectedFilter = selectedFilter,
                    onFilterSelected = { selectedFilter = it }
                )

                Spacer(modifier = Modifier.height(20.dp))

                val visiblePastMatches = when (selectedFilter) {
                    MatchFilter.TODOS -> pastMatches
                    MatchFilter.ME_SUME -> pastMatches.filter { it.isUserJoined }
                    MatchFilter.ORGANIZO -> pastMatches.filter { it.isOrganizer }
                }

                if (visiblePastMatches.isEmpty()) {
                    EmptyFilteredMatches(selectedFilter = selectedFilter)
                } else {
                    visiblePastMatches.forEach { match ->
                        PastMatchCard(
                            match = match,
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
private fun MatchesTabs(
    selectedTab: String,
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
                        color = if (selectedTab == tab) GreenPrimary else Color(0xFF444444)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(
                                if (selectedTab == tab) GreenPrimary else Color.Transparent
                            )
                    )
                }
            }
        }

        HorizontalDivider(
            color = Color(0xFFE0E0E0),
            thickness = 1.dp
        )
    }
}

@Composable
private fun MatchesFilterChips(
    selectedFilter: MatchFilter,
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
                onClick = { onFilterSelected(filter) }
            )
        }
    }
}

@Composable
private fun MatchFilterChip(
    filter: MatchFilter,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (selected) Color(0xFFE2F8E9) else Color.White,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) Color(0xFFB9EFC8) else Color(0xFFE5E2E2)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MatchFilterIcon(
                filter = filter,
                selected = selected
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = filter.label,
                fontSize = 14.sp,
                fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
                color = if (selected) GreenPrimary else Color(0xFF444444)
            )
        }
    }
}

@Composable
private fun MatchFilterIcon(
    filter: MatchFilter,
    selected: Boolean
) {
    when (filter) {
        MatchFilter.TODOS -> {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(if (selected) GreenPrimary else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✓",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selected) Color.White else Color(0xFF4F574C)
                )
            }
        }

        MatchFilter.ME_SUME -> {
            Icon(
                imageVector = Icons.Default.Groups,
                contentDescription = "Me sumé",
                tint = if (selected) GreenPrimary else Color(0xFF4F574C),
                modifier = Modifier.size(18.dp)
            )
        }

        MatchFilter.ORGANIZO -> {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Organizo",
                tint = if (selected) GreenPrimary else Color(0xFF4F574C),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun EmptyFilteredMatches(
    selectedFilter: MatchFilter
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
            color = Color(0xFF8A8A8A)
        )
    }
}

@Composable
private fun UserMatchCard(
    match: UserMatch,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF7F5F5)
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
                    color = Color(0xFF202020),
                    modifier = Modifier.weight(1f)
                )

                StatusChip(status = match.status)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "Ubicación",
                    tint = Color(0xFF4F574C),
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = match.location,
                    fontSize = 14.sp,
                    color = Color(0xFF4F574C)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(
                color = Color(0xFFE8E5E5),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                DateBox(
                    day = match.day,
                    month = match.month
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Hora",
                            tint = Color(0xFF4F574C),
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = match.time,
                            fontSize = 14.sp,
                            color = Color(0xFF202020)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = "Jugadores",
                            tint = Color(0xFF4F574C),
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = match.players,
                            fontSize = 14.sp,
                            color = Color(0xFF4F574C)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                color = Color(0xFFE8E5E5),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Ver detalles >",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary,
                modifier = Modifier.clickable { onClick() }
            )
        }
    }
}
@Composable
private fun PastMatchCard(
    match: PastMatch,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF7F5F5)
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
                    color = Color(0xFF202020),
                    modifier = Modifier.weight(1f)
                )

                FinishedStatusChip()
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "Ubicación",
                    tint = Color(0xFF4F574C),
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = match.location,
                    fontSize = 14.sp,
                    color = Color(0xFF4F574C)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(
                color = Color(0xFFE8E5E5),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DateBox(
                    day = match.day,
                    month = match.month
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
                                tint = Color(0xFF4F574C),
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = match.time,
                                fontSize = 14.sp,
                                color = Color(0xFF202020)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = match.result,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                color = Color(0xFFE8E5E5),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Ver detalles >",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary,
                modifier = Modifier.clickable { onClick() }
            )
        }
    }
}
@Composable
private fun DateBox(
    day: String,
    month: String
) {
    Surface(
        modifier = Modifier.size(width = 62.dp, height = 64.dp),
        shape = RoundedCornerShape(6.dp),
        color = Color(0xFFE5F0E4)
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
                color = GreenPrimary
            )

            Text(
                text = month,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary
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
private fun EmptyPastMatches() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = "Sin partidos pasados",
            tint = Color(0xFFBDBDBD),
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Todavía no tenés partidos pasados.",
            fontSize = 15.sp,
            color = Color(0xFF8A8A8A)
        )
    }
}

@Composable
private fun MatchesBottomBar(
    onHomeClick: () -> Unit,
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
            UnselectedBottomItem(
                icon = Icons.Default.Home,
                label = "Inicio",
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
                onClick = onCreateMatchClick
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
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = Color(0xFF4F574C),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}