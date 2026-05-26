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
import com.matchball.fulbomatch.ui.theme.GreenPrimary
import com.matchball.fulbomatch.ui.components.MoonIconButton

private val ScreenBackground = Color(0xFFFAF9F8)
private val CardBackground = Color.White
private val TextDark = Color(0xFF202020)
private val TextSoft = Color(0xFF4F574C)
private val TeamBlue = Color(0xFF004AAD)
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
    onNotificationsClick: () -> Unit = {}
) {
    val match = getFinishedMatchDetail(matchId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Estadísticas",
                        color = GreenPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = GreenPrimary
                        )
                    }
                },
                actions = {
                    MoonIconButton()

                    IconButton(onClick = onNotificationsClick) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificaciones",
                            tint = GreenPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ScreenBackground
                )
            )
        },
        bottomBar = {
            StatisticsBottomBar(
                onHomeClick = onHomeClick,
                onMatchesClick = onMatchesClick,
                onCreateClick = onCreateClick,
                onProfileClick = onProfileClick
            )
        },
        containerColor = ScreenBackground
    ) { paddingValues ->
        if (match == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Estadísticas no encontradas",
                    fontSize = 16.sp,
                    color = TextDark
                )
            }
        } else {
            StatisticsContent(
                match = match,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
private fun StatisticsContent(
    match: FinishedMatchDetail,
    modifier: Modifier = Modifier
) {
    val stats = match.statistics

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(18.dp))

        ScoreCard(match = match)

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Métricas Clave",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = GreenPrimary
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            MetricCard(
                modifier = Modifier.weight(1f),
                iconContent = {
                    Text(
                        text = "⚽",
                        fontSize = 28.sp,
                        color = Color(0xFF6D6D6D)
                    )
                },
                value = match.totalGoals.toString(),
                label = "Goles Totales"
            )

            MetricCard(
                modifier = Modifier.weight(1f),
                iconContent = {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Duración",
                        tint = Color(0xFF6D6D6D),
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
            color = TextDark
        )

        Spacer(modifier = Modifier.height(14.dp))

        stats.scorers.forEach { scorer ->
            ScorerCard(scorer = scorer)
            Spacer(modifier = Modifier.height(14.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ScoreCard(match: FinishedMatchDetail) {
    val stats = match.statistics

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
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
                color = TextSoft,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stats.dateTimeAndPlace,
                fontSize = 15.sp,
                color = TextSoft,
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
                    color = GreenPrimary
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
                        color = GreenPrimary
                    )

                    Text(
                        text = " - ",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSoft,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )

                    Text(
                        text = match.resultAway,
                        fontSize = 58.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TeamBlue
                    )
                }

                TeamColumn(
                    modifier = Modifier.weight(1f),
                    teamName = stats.teamBName,
                    color = TeamBlue
                )
            }
        }
    }
}

@Composable
private fun TeamColumn(
    modifier: Modifier,
    teamName: String,
    color: Color
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
            color = TextDark,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier,
    iconContent: @Composable () -> Unit,
    value: String,
    label: String
) {
    Card(
        modifier = modifier.height(124.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
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
                color = TextDark
            )

            Text(
                text = label,
                fontSize = 12.sp,
                color = TextSoft,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ScorerCard(scorer: FinishedMatchScorer) {
    val teamColor = getTeamColor(scorer.teamName)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
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
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = teamColor.copy(alpha = 0.12f)
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
                    color = Color(0xFFB9B9B9),
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
                        color = TextDark
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = if (scorer.goals == 1) "Gol" else "Goles",
                        fontSize = 13.sp,
                        color = TextDark
                    )
                }
            }
        }
    }
}

@Composable
private fun StatisticsBottomBar(
    onHomeClick: () -> Unit,
    onMatchesClick: () -> Unit,
    onCreateClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = ScreenBackground,
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
                        tint = TextSoft,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = "Inicio",
                selected = false,
                onClick = onHomeClick
            )

            BottomItem(
                icon = {
                    Text(
                        text = "⚽",
                        fontSize = 22.sp,
                        color = Color.White
                    )
                },
                label = "Partidos",
                selected = true,
                onClick = onMatchesClick
            )

            BottomItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Crear",
                        tint = TextSoft,
                        modifier = Modifier.size(25.dp)
                    )
                },
                label = "Crear",
                selected = false,
                onClick = onCreateClick
            )

            BottomItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = TextSoft,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = "Perfil",
                selected = false,
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
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.width(72.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(28.dp),
            color = if (selected) GreenPrimary else Color.Transparent,
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
            color = TextSoft
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

private fun getTeamColor(teamName: String): Color {
    return when (teamName) {
        "Equipo A" -> GreenPrimary
        "Equipo B" -> TeamBlue
        else -> TextSoft
    }
}