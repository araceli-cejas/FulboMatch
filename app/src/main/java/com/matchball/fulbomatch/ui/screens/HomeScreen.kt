package com.matchball.fulbomatch.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.window.DialogProperties
import com.matchball.fulbomatch.R
import com.matchball.fulbomatch.ui.components.MoonIconButton
import com.matchball.fulbomatch.ui.theme.GreenPrimary
import com.matchball.fulbomatch.ui.theme.White

data class MockMatch(
    val id: String,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val players: String,
    val level: String,
    val status: String,
    val almostFull: Boolean,
    val imageRes: Int,
    val tags: List<String>,
    val distance: String? = null
) {
    val dateTime: String
        get() = "$date $time"
}

val mockMatches = listOf(
    MockMatch(
        id = "1",
        title = "Fútbol 5 en Palermo",
        date = "Vie 25 Oct",
        time = "21:00",
        location = "Palermo, CABA",
        players = "8/10 jugadores",
        level = "Intermedio",
        status = "ABIERTO",
        almostFull = false,
        imageRes = R.drawable.cancha,
        tags = listOf("Césped Natural", "Fútbol 5"),
        distance = "3.5 km"
    ),
    MockMatch(
        id = "2",
        title = "Fútbol 7 Mix",
        date = "Sáb 26 Oct",
        time = "18:30",
        location = "Caballito",
        players = "12/14 jugadores",
        level = "Avanzado",
        status = "CASI LLENO",
        almostFull = true,
        imageRes = R.drawable.cancha,
        tags = listOf("Sintético"),
        distance = "0.5 km"
    )
)

@Composable
fun HomeScreen(
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {},
    onMatchClick: (String) -> Unit,
    onCreateMatchClick: () -> Unit,
    onProfileClick: () -> Unit,
    onRequestsClick: () -> Unit,
    onMatchesClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todos") }
    var selectedLocation by remember { mutableStateOf("Cerca de Caballito, CABA") }
    var showLocationDialog by remember { mutableStateOf(false) }

    val colors = homeColors(isDarkMode)

    val filteredMatches = mockMatches.filter { match ->
        val matchesSearch =
            searchText.isBlank() ||
                    match.title.contains(searchText, ignoreCase = true) ||
                    match.location.contains(searchText, ignoreCase = true)

        val matchesFilter =
            selectedFilter == "Todos" ||
                    match.tags.any { it.equals(selectedFilter, ignoreCase = true) }

        matchesSearch && matchesFilter
    }

    Scaffold(
        bottomBar = {
            HomeBottomBar(
                colors = colors,
                onCreateMatchClick = onCreateMatchClick,
                onProfileClick = onProfileClick,
                onMatchesClick = onMatchesClick
            )
        },
        containerColor = colors.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(colors.background)
                .semantics { contentDescription = "Pantalla principal con partidos recomendados" }
        ) {
            HomeHeader(
                colors = colors,
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
                onNotificationsClick = onRequestsClick
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 18.dp,
                    end = 18.dp,
                    top = 14.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    SearchBar(
                        searchText = searchText,
                        onSearchTextChange = { searchText = it },
                        colors = colors
                    )
                }

                item {
                    FilterRow(
                        selectedFilter = selectedFilter,
                        onFilterSelected = { selectedFilter = it },
                        colors = colors
                    )
                }

                item {
                    LocationInfoRow(
                        selectedLocation = selectedLocation,
                        colors = colors,
                        onChangeLocationClick = {
                            showLocationDialog = true
                        }
                    )
                }

                item {
                    Text(
                        text = "Partidos recomendados",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )
                }

                if (filteredMatches.isEmpty()) {
                    item {
                        Text(
                            text = "No se encontraron partidos con esa búsqueda.",
                            fontSize = 14.sp,
                            color = colors.textSecondary,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                } else {
                    items(filteredMatches) { match ->
                        MatchCard(
                            match = match,
                            colors = colors,
                            onClick = { onMatchClick(match.id) }
                        )
                    }
                }
            }
        }

        if (showLocationDialog) {
            ChangeLocationDialog(
                colors = colors,
                selectedLocation = selectedLocation,
                onLocationSelected = { newLocation ->
                    selectedLocation = newLocation
                    showLocationDialog = false
                },
                onDismiss = {
                    showLocationDialog = false
                }
            )
        }
    }
}

@Composable
private fun HomeHeader(
    colors: HomeColors,
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
            .background(colors.headerBackground)
            .padding(horizontal = 20.dp, vertical = 12.dp),
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
private fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    colors: HomeColors
) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        placeholder = {
            Text(
                text = "Buscar partido por zona",
                color = colors.textSecondary,
                fontSize = 14.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = colors.icon
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GreenPrimary,
            unfocusedBorderColor = colors.border,
            focusedTextColor = colors.textPrimary,
            unfocusedTextColor = colors.textPrimary,
            cursorColor = GreenPrimary,
            focusedContainerColor = colors.inputBackground,
            unfocusedContainerColor = colors.inputBackground
        )
    )
}

@Composable
private fun FilterRow(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    colors: HomeColors
) {
    val filters = listOf("Todos", "Césped Natural", "Sintético", "Fútbol 5")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            FilterPill(
                text = filter,
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                colors = colors
            )
        }
    }
}

@Composable
private fun LocationInfoRow(
    selectedLocation: String,
    colors: HomeColors,
    onChangeLocationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = "Ubicación actual",
                tint = colors.locationText,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = selectedLocation,
                color = colors.locationText,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Text(
            text = "Cambiar",
            color = colors.textSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable {
                onChangeLocationClick()
            }
        )
    }
}

@Composable
private fun ChangeLocationDialog(
    colors: HomeColors,
    selectedLocation: String,
    onLocationSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val locations = listOf(
        "Cerca de Caballito, CABA",
        "Cerca de Palermo, CABA",
        "Cerca de Villa Crespo, CABA",
        "Usar ubicación actual"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.cardBackground,
        title = {
            Text(
                text = "Cambiar ubicación",
                color = colors.textPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "Seleccioná una zona para ver partidos recomendados.",
                    color = colors.textSecondary,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                locations.forEach { location ->
                    LocationDialogOption(
                        text = location,
                        selected = selectedLocation == location,
                        colors = colors,
                        onClick = {
                            val newLocation = if (location == "Usar ubicación actual") {
                                "Cerca de Caballito, CABA"
                            } else {
                                location
                            }

                            onLocationSelected(newLocation)
                        }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cerrar",
                    color = colors.locationText,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        )
    )
}

@Composable
private fun LocationDialogOption(
    text: String,
    selected: Boolean,
    colors: HomeColors,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Place,
            contentDescription = null,
            tint = if (selected) colors.locationText else colors.textSecondary,
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            color = if (selected) colors.locationText else colors.textPrimary,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun FilterPill(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    colors: HomeColors
) {
    Surface(
        modifier = Modifier
            .height(36.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        color = if (selected) colors.selectedChipBackground else colors.cardBackground,
        border = if (selected) null else BorderStroke(1.dp, colors.border)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 13.sp,
                color = if (selected) colors.selectedChipText else colors.textPrimary,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun MatchCard(
    match: MockMatch,
    colors: HomeColors,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                Image(
                    painter = painterResource(id = match.imageRes),
                    contentDescription = "Imagen del partido",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(
                            RoundedCornerShape(
                                topStart = 12.dp,
                                topEnd = 12.dp
                            )
                        ),
                    contentScale = ContentScale.Crop
                )

                StatusBadge(
                    text = match.status,
                    almostFull = match.almostFull,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                )
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = match.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.textPrimary,
                        modifier = Modifier.weight(1f)
                    )

                    match.distance?.let { distance ->
                        DistanceBadge(
                            text = distance,
                            colors = colors
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Día y horario",
                        tint = colors.icon,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = match.dateTime,
                        fontSize = 13.sp,
                        color = colors.textSecondary
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "Ubicación",
                        tint = colors.icon,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = match.location,
                        fontSize = 13.sp,
                        color = colors.textSecondary
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                HorizontalDivider(
                    color = colors.divider,
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AvatarStack(colors = colors)

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = match.players,
                        fontSize = 13.sp,
                        color = colors.textSecondary,
                        modifier = Modifier.weight(1f)
                    )

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = colors.levelBackground
                    ) {
                        Text(
                            text = match.level,
                            fontSize = 11.sp,
                            color = colors.textPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.selectedChipBackground,
                        contentColor = colors.selectedChipText
                    )
                ) {
                    Text(
                        text = "Ver detalle",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(
    text: String,
    almostFull: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = if (almostFull) Color(0xFF0057A8) else GreenPrimary
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            color = White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun DistanceBadge(
    text: String,
    colors: HomeColors
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = colors.distanceBadgeBackground
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            color = colors.distanceBadgeText,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun AvatarStack(
    colors: HomeColors
) {
    Row {
        repeat(3) { index ->
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(
                        when (index) {
                            0 -> Color(0xFF005C1F)
                            1 -> Color(0xFF007A3D)
                            else -> Color(0xFF4AAE73)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "👤",
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.width(2.dp))
        }

        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(colors.levelBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+10",
                fontSize = 8.sp,
                color = colors.textPrimary
            )
        }
    }
}

@Composable
private fun HomeBottomBar(
    colors: HomeColors,
    onCreateMatchClick: () -> Unit,
    onProfileClick: () -> Unit,
    onMatchesClick: () -> Unit
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
            SelectedBottomItem(
                icon = Icons.Default.Home,
                label = "Inicio",
                onClick = { }
            )

            UnselectedBottomItem(
                iconText = "⚽",
                label = "Partidos",
                colors = colors,
                onClick = onMatchesClick
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
    colors: HomeColors,
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
    colors: HomeColors,
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

private data class HomeColors(
    val background: Color,
    val headerBackground: Color,
    val cardBackground: Color,
    val inputBackground: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val icon: Color,
    val headerIcon: Color,
    val border: Color,
    val divider: Color,
    val levelBackground: Color,
    val bottomBarBackground: Color,
    val bottomIcon: Color,
    val selectedChipBackground: Color,
    val selectedChipText: Color,
    val locationText: Color,
    val distanceBadgeBackground: Color,
    val distanceBadgeText: Color
)

private fun homeColors(isDarkMode: Boolean): HomeColors {
    return if (isDarkMode) {
        HomeColors(
            background = Color(0xFF111111),
            headerBackground = Color(0xFF111111),
            cardBackground = Color(0xFF1A1A1A),
            inputBackground = Color(0xFF1A1A1A),
            textPrimary = Color(0xFFEDEDED),
            textSecondary = Color(0xFFBDBDBD),
            icon = Color(0xFFC9D1C9),
            headerIcon = Color(0xFFC9D1C9),
            border = Color(0xFF3E463E),
            divider = Color(0xFF2A2A2A),
            levelBackground = Color(0xFF333333),
            bottomBarBackground = Color(0xFF151515),
            bottomIcon = Color(0xFFC9D1C9),
            selectedChipBackground = Color(0xFF9EF49B),
            selectedChipText = Color(0xFF111111),
            locationText = Color(0xFF9EF49B),
            distanceBadgeBackground = Color(0xFFB8F29B),
            distanceBadgeText = Color(0xFF1E6B2D)
        )
    } else {
        HomeColors(
            background = Color(0xFFF6F6F6),
            headerBackground = White,
            cardBackground = White,
            inputBackground = White,
            textPrimary = Color.Black,
            textSecondary = Color(0xFF555555),
            icon = Color(0xFF333333),
            headerIcon = GreenPrimary,
            border = Color(0xFFD0D0D0),
            divider = Color(0xFFE0E0E0),
            levelBackground = Color(0xFFEDEBEB),
            bottomBarBackground = Color(0xFFF3F1F1),
            bottomIcon = Color(0xFF4F574C),
            selectedChipBackground = GreenPrimary,
            selectedChipText = White,
            locationText = GreenPrimary,
            distanceBadgeBackground = Color(0xFFE6F7E7),
            distanceBadgeText = Color(0xFF1E6B2D)
        )
    }
}