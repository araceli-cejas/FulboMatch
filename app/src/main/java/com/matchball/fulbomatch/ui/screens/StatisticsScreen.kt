package com.matchball.fulbomatch.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matchball.fulbomatch.ui.components.MoonIconButton
import com.matchball.fulbomatch.ui.theme.GreenPrimary

private data class StatisticsColors(
    val background: Color,
    val cardBackground: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val headerIcon: Color,
    val icon: Color,
    val accent: Color,
    val accentText: Color,
    val teamBlue: Color,
    val divider: Color,
    val border: Color,
    val bottomBarBackground: Color,
    val bottomIcon: Color
)

private fun statisticsColors(isDarkMode: Boolean): StatisticsColors {
    return if (isDarkMode) {
        StatisticsColors(
            background = Color(0xFF111111),
            cardBackground = Color(0xFF1A1A1A),
            textPrimary = Color(0xFFEDEDED),
            textSecondary = Color(0xFFBDBDBD),
            textMuted = Color(0xFF9E9E9E),
            headerIcon = Color(0xFFC9D1C9),
            icon = Color(0xFFC9D1C9),
            accent = Color(0xFF9EF49B),
            accentText = Color(0xFF111111),
            teamBlue = Color(0xFF4D8DFF),
            divider = Color(0xFF2A2A2A),
            border = Color(0xFF555555),
            bottomBarBackground = Color(0xFF151515),
            bottomIcon = Color(0xFFC9D1C9)
        )
    } else {
        StatisticsColors(
            background = Color(0xFFFAF9F8),
            cardBackground = Color.White,
            textPrimary = Color(0xFF202020),
            textSecondary = Color(0xFF4F574C),
            textMuted = Color(0xFF6D6D6D),
            headerIcon = GreenPrimary,
            icon = Color(0xFF6D6D6D),
            accent = GreenPrimary,
            accentText = Color.White,
            teamBlue = Color(0xFF004AAD),
            divider = Color(0xFFE0E0E0),
            border = Color(0xFFB9B9B9),
            bottomBarBackground = Color(0xFFFAF9F8),
            bottomIcon = Color(0xFF4F574C)
        )
    }
}

private val YellowCard = Color(0xFFE0A800)
private val RedCard = Color(0xFFFF4D4D)

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
    onToggleDarkMode: () -> Unit = {}
) {
    val match = getFinishedMatchDetail(matchId)
    val colors = statisticsColors(isDarkMode)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Estadísticas",
                        color = colors.textPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
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
            StatisticsBottomBar(
                colors = colors,
                onHomeClick = onHomeClick,
                onMatchesClick = onMatchesClick,
                onCreateClick = onCreateClick,
                onProfileClick = onProfileClick
            )
        },
        containerColor = colors.background
    ) { paddingValues ->
        if (match == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.background)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Estadísticas no encontradas",
                    fontSize = 16.sp,
                    color = colors.textPrimary
                )
            }
        } else {
            StatisticsContent(
                match = match,
                colors = colors,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun StatisticsContent(
    match: FinishedMatchDetail,
    colors: StatisticsColors,
    modifier: Modifier = Modifier
) {
    val stats = match.statistics

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(18.dp))

        ScoreCard(
            match = match,
            colors = colors
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Métricas Clave",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            MetricCard(
                modifier = Modifier.weight(1f),
                colors = colors,
                iconContent = {
                    Text(
                        text = "⚽",
                        fontSize = 28.sp,
                        color = colors.icon
                    )
                },
                value = match.totalGoals.toString(),
                label = "Goles Totales"
            )

            MetricCard(
                modifier = Modifier.weight(1f),
                colors = colors,
                iconContent = {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Duración",
                        tint = colors.icon,
                        modifier = Modifier.size(27.dp)
                    )
                },
                value = "${stats.duration} min",
                label = "Duración"
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            MetricCard(
                modifier = Modifier.weight(1f),
                colors = colors,
                iconContent = {
                    CardPenaltyIcon(
                        modifier = Modifier.size(30.dp),
                        color = YellowCard
                    )
                },
                value = stats.yellowCards.toString(),
                label = "Tarjetas Amarillas"
            )

            MetricCard(
                modifier = Modifier.weight(1f),
                colors = colors,
                iconContent = {
                    CardPenaltyIcon(
                        modifier = Modifier.size(30.dp),
                        color = RedCard
                    )
                },
                value = stats.redCards.toString(),
                label = "Tarjetas Rojas"
            )
        }

        Spacer(modifier = Modifier.height(34.dp))

        Text(
            text = "Goleadores",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(14.dp))

        stats.scorers.forEach { scorer ->
            ScorerCard(
                scorer = scorer,
                colors = colors
            )

            Spacer(modifier = Modifier.height(14.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ScoreCard(
    match: FinishedMatchDetail,
    colors: StatisticsColors
) {
    val stats = match.statistics

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "FINALIZADO",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = colors.textSecondary,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stats.dateTimeAndPlace,
                fontSize = 15.sp,
                color = colors.textSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TeamColumn(
                    modifier = Modifier.weight(1f),
                    teamName = stats.teamAName,
                    color = colors.accent,
                    colors = colors
                )

                Row(
                    modifier = Modifier.weight(1.1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = match.resultHome,
                        fontSize = 58.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = colors.accent
                    )

                    Text(
                        text = " - ",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.textSecondary,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )

                    Text(
                        text = match.resultAway,
                        fontSize = 58.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = colors.teamBlue
                    )
                }

                TeamColumn(
                    modifier = Modifier.weight(1f),
                    teamName = stats.teamBName,
                    color = colors.teamBlue,
                    colors = colors
                )
            }
        }
    }
}

@Composable
private fun TeamColumn(
    modifier: Modifier,
    teamName: String,
    color: Color,
    colors: StatisticsColors
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "⚽",
                fontSize = 27.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = teamName,
            fontSize = 21.sp,
            fontWeight = FontWeight.Medium,
            color = colors.textPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier,
    colors: StatisticsColors,
    iconContent: @Composable () -> Unit,
    value: String,
    label: String
) {
    Card(
        modifier = modifier.height(124.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            iconContent()

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )

            Text(
                text = label,
                fontSize = 12.sp,
                color = colors.textSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ScorerCard(
    scorer: FinishedMatchScorer,
    colors: StatisticsColors
) {
    val teamColor = getTeamColor(
        teamName = scorer.teamName,
        colors = colors
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF22302B)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = scorer.name,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = scorer.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.textPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = teamColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = scorer.teamName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = teamColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.Transparent,
                modifier = Modifier.border(
                    width = 1.dp,
                    color = colors.border,
                    shape = RoundedCornerShape(8.dp)
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = scorer.goals.toString(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = if (scorer.goals == 1) "Gol" else "Goles",
                        fontSize = 13.sp,
                        color = colors.textPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun StatisticsBottomBar(
    colors: StatisticsColors,
    onHomeClick: () -> Unit,
    onMatchesClick: () -> Unit,
    onCreateClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = colors.bottomBarBackground,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 22.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Inicio",
                        tint = colors.bottomIcon,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = "Inicio",
                selected = false,
                colors = colors,
                onClick = onHomeClick
            )

            BottomItem(
                icon = {
                    Text(
                        text = "⚽",
                        fontSize = 22.sp,
                        color = if (true) colors.accentText else colors.bottomIcon
                    )
                },
                label = "Partidos",
                selected = true,
                colors = colors,
                onClick = onMatchesClick
            )

            BottomItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Crear",
                        tint = colors.bottomIcon,
                        modifier = Modifier.size(25.dp)
                    )
                },
                label = "Crear",
                selected = false,
                colors = colors,
                onClick = onCreateClick
            )

            BottomItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = colors.bottomIcon,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = "Perfil",
                selected = false,
                colors = colors,
                onClick = onProfileClick
            )
        }
    }
}

@Composable
private fun BottomItem(
    icon: @Composable () -> Unit,
    label: String,
    selected: Boolean,
    colors: StatisticsColors,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.width(72.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(28.dp),
            color = if (selected) colors.accent else Color.Transparent,
            modifier = Modifier
                .height(44.dp)
                .width(if (selected) 78.dp else 44.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                icon()
            }
        }

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = label,
            fontSize = 11.sp,
            color = colors.bottomIcon
        )
    }
}

@Composable
private fun CardPenaltyIcon(
    modifier: Modifier = Modifier,
    color: Color
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 2.3.dp.toPx()

        rotate(degrees = -18f) {
            drawRoundRect(
                color = color,
                topLeft = Offset(size.width * 0.42f, size.height * 0.10f),
                size = Size(size.width * 0.34f, size.height * 0.58f),
                cornerRadius = CornerRadius(5f, 5f),
                style = Stroke(width = strokeWidth)
            )
        }

        drawLine(
            color = color,
            start = Offset(size.width * 0.15f, size.height * 0.78f),
            end = Offset(size.width * 0.26f, size.height * 0.62f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        drawLine(
            color = color,
            start = Offset(size.width * 0.28f, size.height * 0.84f),
            end = Offset(size.width * 0.38f, size.height * 0.64f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )

        drawLine(
            color = color,
            start = Offset(size.width * 0.06f, size.height * 0.60f),
            end = Offset(size.width * 0.16f, size.height * 0.49f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

private fun getTeamColor(
    teamName: String,
    colors: StatisticsColors
): Color {
    return when (teamName) {
        "Equipo A" -> colors.accent
        "Equipo B" -> colors.teamBlue
        else -> colors.textSecondary
    }
}