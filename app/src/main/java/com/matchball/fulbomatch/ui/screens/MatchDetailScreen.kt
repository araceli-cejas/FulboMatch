package com.matchball.fulbomatch.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matchball.fulbomatch.ui.components.MoonIconButton
import com.matchball.fulbomatch.ui.theme.GreenPrimary
import com.matchball.fulbomatch.ui.theme.White

private data class MatchDetailColors(
    val background: Color,
    val card: Color,
    val innerCard: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val divider: Color,
    val headerIcon: Color,
    val accent: Color,
    val accentText: Color,
    val chipBackground: Color,
    val chipText: Color,
    val playerBackground: Color,
    val playerInactiveBorder: Color,
    val bottomBar: Color,
    val danger: Color
)

private fun matchDetailColors(isDarkMode: Boolean): MatchDetailColors {
    return if (isDarkMode) {
        MatchDetailColors(
            background = Color(0xFF111111),
            card = Color(0xFF1E1E1E),
            innerCard = Color(0xFF2A2A2A),
            textPrimary = Color(0xFFF2F2F2),
            textSecondary = Color(0xFFBDBDBD),
            textMuted = Color(0xFF9E9E9E),
            divider = Color(0xFF333333),
            headerIcon = Color(0xFFC9D1C9),
            accent = Color(0xFF9EF49B),
            accentText = Color(0xFF111111),
            chipBackground = Color(0xFF176B2A),
            chipText = Color(0xFFBDE8B9),
            playerBackground = Color(0xFF2E2E2E),
            playerInactiveBorder = Color(0xFF6A6A6A),
            bottomBar = Color(0xFF1A1A1A),
            danger = Color(0xFFFF6B6B)
        )
    } else {
        MatchDetailColors(
            background = Color(0xFFFAF9F8),
            card = Color(0xFFF1EFEF),
            innerCard = Color(0xFFE7E4E4),
            textPrimary = Color(0xFF202020),
            textSecondary = Color(0xFF4F574C),
            textMuted = Color(0xFF6F6F6F),
            divider = Color(0xFFE0E0E0),
            headerIcon = GreenPrimary,
            accent = GreenPrimary,
            accentText = White,
            chipBackground = GreenPrimary,
            chipText = Color(0xFFBDE8B9),
            playerBackground = Color(0xFFE9E7E7),
            playerInactiveBorder = Color(0xFFBFC6BC),
            bottomBar = Color(0xFFFAF9F8),
            danger = Color(0xFFD32F2F)
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
    isUserJoined: Boolean = false,
    isOrganizer: Boolean = false,
    isFinished: Boolean = false,
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {}
) {
    val match = mockMatches.find { it.id == matchId }
    val finishedMatch = getFinishedMatchDetail(matchId)
    val colors = matchDetailColors(isDarkMode)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalle partido",
                        color = colors.textPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 21.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = colors.headerIcon
                        )
                    }
                },
                actions = {
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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.background
                )
            )
        },
        bottomBar = {
            if (!isFinished && match != null) {
                BottomMatchActionButton(
                    isUserJoined = isUserJoined,
                    isOrganizer = isOrganizer,
                    colors = colors,
                    onJoinClick = onJoinClick,
                    onLeaveClick = onLeaveClick,
                    onEditClick = {
                        onEditMatchClick(matchId)
                    }
                )
            }
        },
        containerColor = colors.background
    ) { padding ->
        when {
            isFinished -> {
                if (finishedMatch == null) {
                    EmptyDetailMessage(
                        text = "Partido finalizado no encontrado",
                        colors = colors,
                        modifier = Modifier.padding(padding)
                    )
                } else {
                    FinishedMatchContent(
                        match = finishedMatch,
                        colors = colors,
                        modifier = Modifier.padding(padding),
                        onStatisticsClick = onStatisticsClick
                    )
                }
            }

            match == null -> {
                EmptyDetailMessage(
                    text = "Partido no encontrado",
                    colors = colors,
                    modifier = Modifier.padding(padding)
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(colors.background)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 18.dp)
                        .semantics { contentDescription = "Detalle del partido" }
                ) {
                    MatchHeroCard(
                        match = match,
                        isUserJoined = isUserJoined,
                        colors = colors
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    OrganizerCard(colors = colors)

                    Spacer(modifier = Modifier.height(20.dp))

                    MeetingDetailsCard(
                        match = match,
                        colors = colors
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    RulesCard(colors = colors)

                    Spacer(modifier = Modifier.height(20.dp))

                    ConfirmedPlayersCard(
                        match = match,
                        colors = colors
                    )

                    Spacer(modifier = Modifier.height(36.dp))
                }
            }
        }
    }
}

@Composable
private fun EmptyDetailMessage(
    text: String,
    colors: MatchDetailColors,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = colors.textPrimary
        )
    }
}

@Composable
private fun MatchHeroCard(
    match: MockMatch,
    isUserJoined: Boolean,
    colors: MatchDetailColors
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.card
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 55.dp, y = (-70).dp)
                    .size(170.dp)
                    .clip(CircleShape)
                    .background(colors.accent.copy(alpha = 0.16f))
            )

            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = colors.chipBackground
                    ) {
                        Text(
                            text = match.status,
                            color = colors.chipText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                        )
                    }

                    if (isUserJoined) {
                        Spacer(modifier = Modifier.width(8.dp))

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = colors.innerCard
                        ) {
                            Text(
                                text = "ANOTADO",
                                color = colors.accent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = "$1500 / jug",
                            color = colors.accent,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.End,
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = match.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "Ubicación",
                        tint = colors.textSecondary,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = match.location,
                        fontSize = 15.sp,
                        color = colors.textSecondary
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DetailSmallBox(
                        modifier = Modifier.weight(1f),
                        colors = colors,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Fecha",
                                tint = colors.accent,
                                modifier = Modifier.size(26.dp)
                            )
                        },
                        text = match.date
                    )

                    DetailSmallBox(
                        modifier = Modifier.weight(1f),
                        colors = colors,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "Hora",
                                tint = colors.accent,
                                modifier = Modifier.size(26.dp)
                            )
                        },
                        text = "${match.time} hs"
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailSmallBox(
    modifier: Modifier = Modifier,
    colors: MatchDetailColors,
    icon: @Composable () -> Unit,
    text: String
) {
    Surface(
        modifier = modifier.height(68.dp),
        shape = RoundedCornerShape(8.dp),
        color = colors.innerCard
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            icon()

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = text,
                fontSize = 15.sp,
                color = colors.textPrimary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun OrganizerCard(
    colors: MatchDetailColors
) {
    DetailSectionCard(colors = colors) {
        Text(
            text = "Organizador",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(color = colors.divider)

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(colors.playerBackground)
                    .border(2.dp, colors.accent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Organizador",
                    tint = colors.accent,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = "Martín G.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "★",
                        color = colors.accent,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "4.8 (12 partidos)",
                        color = colors.textSecondary,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun MeetingDetailsCard(
    match: MockMatch,
    colors: MatchDetailColors
) {
    DetailSectionCard(colors = colors) {
        Text(
            text = "Detalles del Encuentro",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(color = colors.divider)

        Spacer(modifier = Modifier.height(16.dp))

        DetailInfoRow(
            colors = colors,
            icon = {
                SurfaceGrassIcon(
                    modifier = Modifier.size(28.dp),
                    color = colors.accent
                )
            },
            title = "Superficie",
            description = match.tags.firstOrNull() ?: "Césped Sintético"
        )

        Spacer(modifier = Modifier.height(16.dp))

        DetailInfoRow(
            colors = colors,
            icon = {
                Text(
                    text = "♙",
                    fontSize = 24.sp,
                    color = colors.accent
                )
            },
            title = "Nivel sugerido",
            description = match.level
        )

        Spacer(modifier = Modifier.height(16.dp))

        DetailInfoRow(
            colors = colors,
            icon = {
                Text(
                    text = "✓",
                    fontSize = 24.sp,
                    color = colors.accent
                )
            },
            title = "Incluye",
            description = "Pelota, pecheras y bebidas hidratantes\n(opcional)"
        )
    }
}

@Composable
private fun DetailInfoRow(
    colors: MatchDetailColors,
    icon: @Composable () -> Unit,
    title: String,
    description: String
) {
    Row(
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier.width(34.dp),
            contentAlignment = Alignment.TopStart
        ) {
            icon()
        }

        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )

            Text(
                text = description,
                fontSize = 15.sp,
                color = colors.textSecondary,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun RulesCard(
    colors: MatchDetailColors
) {
    DetailSectionCard(colors = colors) {
        Text(
            text = "Reglas",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(color = colors.divider)

        Spacer(modifier = Modifier.height(16.dp))

        RuleItem("Llegar 15 min antes.", colors)
        RuleItem("Confirmación obligatoria 24hs antes.", colors)
        RuleItem("Se juega con lluvia leve, se suspende por\ntormenta.", colors)
        RuleItem("Buena onda, cero mala leche.", colors)
    }
}

@Composable
private fun RuleItem(
    text: String,
    colors: MatchDetailColors
) {
    Row(
        modifier = Modifier.padding(bottom = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "•",
            color = colors.accent,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(18.dp)
        )

        Text(
            text = text,
            fontSize = 15.sp,
            color = colors.textSecondary,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun ConfirmedPlayersCard(
    match: MockMatch,
    colors: MatchDetailColors
) {
    val currentPlayers = getCurrentPlayers(match.players)
    val maxPlayers = getMaxPlayers(match.players)

    DetailSectionCard(colors = colors) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Jugadores Confirmados",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                modifier = Modifier.weight(1f)
            )

            Surface(
                shape = RoundedCornerShape(18.dp),
                color = colors.innerCard
            ) {
                Text(
                    text = "$currentPlayers / $maxPlayers",
                    color = colors.accent,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PlayerSlot(name = "Martín", active = true, organizer = true, colors = colors)
                PlayerSlot(name = "Leo M.", active = true, colors = colors)
                PlayerSlot(name = "Alej.", active = true, letter = "A", colors = colors)
                PlayerSlot(name = "Nico", active = true, colors = colors)
                PlayerSlot(name = "Fede", active = true, colors = colors)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PlayerSlot(name = "Tomi", active = true, colors = colors)
                PlayerSlot(name = "Juan", active = true, colors = colors)
                PlayerSlot(name = "Lucas", active = true, colors = colors)
                PlayerSlot(name = "Libre", active = false, colors = colors)
                PlayerSlot(name = "Libre", active = false, colors = colors)
            }
        }
    }
}

@Composable
private fun PlayerSlot(
    name: String,
    active: Boolean,
    colors: MatchDetailColors,
    organizer: Boolean = false,
    letter: String? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(56.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                    if (active) colors.playerBackground else Color.Transparent
                )
                .then(
                    if (organizer) {
                        Modifier.border(2.dp, colors.accent, CircleShape)
                    } else if (!active) {
                        Modifier.border(1.dp, colors.playerInactiveBorder, CircleShape)
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            when {
                organizer -> {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = name,
                        tint = colors.accent,
                        modifier = Modifier.size(24.dp)
                    )
                }

                letter != null -> {
                    Text(
                        text = letter,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textMuted
                    )
                }

                active -> {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = name,
                        tint = colors.textMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }

                else -> {
                    Text(
                        text = "+",
                        fontSize = 20.sp,
                        color = colors.playerInactiveBorder
                    )
                }
            }

            if (organizer) {
                Text(
                    text = "★",
                    color = colors.accent,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .background(colors.playerBackground, CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = name,
            fontSize = 12.sp,
            fontWeight = if (organizer) FontWeight.Bold else FontWeight.Normal,
            color = if (active) {
                if (organizer) colors.accent else colors.textSecondary
            } else {
                colors.playerInactiveBorder
            },
            maxLines = 1
        )
    }
}

@Composable
private fun DetailSectionCard(
    colors: MatchDetailColors,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.card
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            content = content
        )
    }
}

@Composable
private fun FinishedMatchContent(
    match: FinishedMatchDetail,
    colors: MatchDetailColors,
    modifier: Modifier = Modifier,
    onStatisticsClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 18.dp)
            .semantics { contentDescription = "Detalle del partido finalizado" }
    ) {
        FinishedMatchHeroCard(
            match = match,
            colors = colors
        )

        Spacer(modifier = Modifier.height(18.dp))

        FinishedOrganizerCard(
            match = match,
            colors = colors
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FinishedInfoCard(
                modifier = Modifier.weight(1f),
                colors = colors,
                icon = {
                    SurfaceGrassIcon(
                        modifier = Modifier.size(28.dp),
                        color = colors.accent
                    )
                },
                title = "SUPERFICIE",
                description = match.surface
            )

            FinishedInfoCard(
                modifier = Modifier.weight(1f),
                colors = colors,
                icon = {
                    Text(
                        text = "★",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.accent
                    )
                },
                title = "NIVEL",
                description = match.level
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        FinishedPlayersCard(
            match = match,
            colors = colors
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onStatisticsClick(match.id)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.accent,
                contentColor = colors.accentText
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatsBarsIcon(
                    modifier = Modifier.size(20.dp),
                    color = colors.accentText
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Ver estadísticas",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))
    }
}

@Composable
private fun FinishedMatchHeroCard(
    match: FinishedMatchDetail,
    colors: MatchDetailColors
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.card
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = match.title,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        lineHeight = 31.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "Ubicación",
                            tint = colors.textSecondary,
                            modifier = Modifier.size(17.dp)
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            text = match.location,
                            fontSize = 14.sp,
                            color = colors.textSecondary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = colors.innerCard,
                    border = BorderStroke(1.dp, colors.divider)
                ) {
                    Text(
                        text = "FINALIZADO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textMuted,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = colors.innerCard
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.width(92.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Fecha",
                                tint = colors.textSecondary,
                                modifier = Modifier.size(16.dp)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = match.date,
                                fontSize = 12.sp,
                                color = colors.textSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "Hora",
                                tint = colors.textSecondary,
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = match.time,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                color = colors.textPrimary,
                                lineHeight = 22.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        color = colors.background
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "RESULTADO",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.textMuted
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = match.resultHome,
                                    fontSize = 42.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.accent
                                )

                                Text(
                                    text = " - ",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.textMuted
                                )

                                Text(
                                    text = match.resultAway,
                                    fontSize = 42.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.textPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FinishedOrganizerCard(
    match: FinishedMatchDetail,
    colors: MatchDetailColors
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.card
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(colors.playerBackground)
                    .border(2.dp, colors.accent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Organizador",
                    tint = colors.accent,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "ORGANIZADOR",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textMuted
                )

                Text(
                    text = match.organizerName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "★ ${match.rating}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.accent
                )

                Text(
                    text = match.ratingDescription,
                    fontSize = 12.sp,
                    color = colors.textSecondary
                )
            }
        }
    }
}

@Composable
private fun FinishedInfoCard(
    modifier: Modifier = Modifier,
    colors: MatchDetailColors,
    icon: @Composable () -> Unit,
    title: String,
    description: String
) {
    Card(
        modifier = modifier.height(126.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.card
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            icon()

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textMuted
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                fontSize = 16.sp,
                color = colors.textPrimary,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun FinishedPlayersCard(
    match: FinishedMatchDetail,
    colors: MatchDetailColors
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.card
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "👥  Jugadores",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = colors.innerCard
                ) {
                    Text(
                        text = "${match.players} Confirmados",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textMuted,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PlayerSlot(name = "Juan P.", active = true, organizer = true, colors = colors)
                PlayerSlot(name = "Matías", active = true, colors = colors)
                PlayerSlot(name = "Sofi", active = true, letter = "S", colors = colors)
                PlayerSlot(name = "Marcos", active = true, letter = "M", colors = colors)
                PlayerSlot(name = "Fede", active = true, colors = colors)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PlayerSlot(name = "Lucas", active = true, colors = colors)
                PlayerSlot(name = "Gastón", active = true, letter = "G", colors = colors)
                PlayerSlot(name = "Pablo", active = true, colors = colors)
                PlayerSlot(name = "Nico", active = true, colors = colors)
                PlayerSlot(name = "Ezequiel", active = true, letter = "E", colors = colors)
            }
        }
    }
}

@Composable
private fun BottomMatchActionButton(
    isUserJoined: Boolean,
    isOrganizer: Boolean,
    colors: MatchDetailColors,
    onJoinClick: () -> Unit,
    onLeaveClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = colors.bottomBar,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 10.dp,
                    bottom = 28.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isOrganizer) {
                Button(
                    onClick = onEditClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.accent,
                        contentColor = colors.accentText
                    )
                ) {
                    Text(
                        text = "Editar partido",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Solo el organizador puede modificar o cancelar este partido.",
                    fontSize = 13.sp,
                    color = colors.textMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else if (isUserJoined) {
                OutlinedButton(
                    onClick = onLeaveClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, colors.danger),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colors.danger
                    )
                ) {
                    Text(
                        text = "Bajarme del partido",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Se liberará tu cupo y el organizador será notificado.",
                    fontSize = 13.sp,
                    color = colors.textMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Button(
                    onClick = onJoinClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.accent,
                        contentColor = colors.accentText
                    )
                ) {
                    Text(
                        text = "Sumarme al partido",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "⚽",
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SurfaceGrassIcon(
    modifier: Modifier = Modifier,
    color: Color = GreenPrimary
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 2.4.dp.toPx()

        drawLine(
            color = color,
            start = Offset(size.width * 0.15f, size.height * 0.78f),
            end = Offset(size.width * 0.85f, size.height * 0.78f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        val centerGrass = Path().apply {
            moveTo(size.width * 0.50f, size.height * 0.78f)
            quadraticTo(
                size.width * 0.46f,
                size.height * 0.45f,
                size.width * 0.34f,
                size.height * 0.28f
            )
        }

        drawPath(
            path = centerGrass,
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )

        val leftGrass = Path().apply {
            moveTo(size.width * 0.36f, size.height * 0.78f)
            quadraticTo(
                size.width * 0.26f,
                size.height * 0.56f,
                size.width * 0.14f,
                size.height * 0.50f
            )
        }

        drawPath(
            path = leftGrass,
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )

        val rightGrass = Path().apply {
            moveTo(size.width * 0.62f, size.height * 0.78f)
            quadraticTo(
                size.width * 0.72f,
                size.height * 0.56f,
                size.width * 0.86f,
                size.height * 0.48f
            )
        }

        drawPath(
            path = rightGrass,
            color = color,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )
    }
}

@Composable
private fun StatsBarsIcon(
    modifier: Modifier = Modifier,
    color: Color = White
) {
    Canvas(modifier = modifier) {
        val barWidth = size.width * 0.18f
        val radius = barWidth / 2

        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.16f, size.height * 0.58f),
            size = Size(barWidth, size.height * 0.30f),
            cornerRadius = CornerRadius(radius, radius)
        )

        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.41f, size.height * 0.36f),
            size = Size(barWidth, size.height * 0.52f),
            cornerRadius = CornerRadius(radius, radius)
        )

        drawRoundRect(
            color = color,
            topLeft = Offset(size.width * 0.66f, size.height * 0.18f),
            size = Size(barWidth, size.height * 0.70f),
            cornerRadius = CornerRadius(radius, radius)
        )
    }
}

private fun getCurrentPlayers(players: String): Int {
    return players.substringBefore("/")
        .trim()
        .toIntOrNull() ?: 8
}

private fun getMaxPlayers(players: String): Int {
    return players.substringAfter("/")
        .substringBefore(" ")
        .trim()
        .toIntOrNull() ?: 10
}