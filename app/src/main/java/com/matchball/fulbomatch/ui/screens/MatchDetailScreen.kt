package com.matchball.fulbomatch.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matchball.fulbomatch.ui.theme.GreenPrimary
import com.matchball.fulbomatch.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDetailScreen(
    matchId: String,
    onBackClick: () -> Unit,
    onJoinClick: () -> Unit = {}
) {
    val match = mockMatches.find { it.id == matchId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalle partido",
                        color = GreenPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 21.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Text(
                            text = "☾",
                            fontSize = 24.sp,
                            color = GreenPrimary
                        )
                    }

                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificaciones",
                            tint = GreenPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFAF9F8)
                )
            )
        },
        bottomBar = {
            if (match != null) {
                BottomJoinButton(onJoinClick = onJoinClick)
            }
        },
        containerColor = Color(0xFFFAF9F8)
    ) { padding ->
        if (match == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Partido no encontrado",
                    fontSize = 16.sp
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 18.dp)
                    .semantics { contentDescription = "Detalle del partido" }
            ) {
                MatchHeroCard(match = match)

                Spacer(modifier = Modifier.height(20.dp))

                OrganizerCard()

                Spacer(modifier = Modifier.height(20.dp))

                MeetingDetailsCard(match = match)

                Spacer(modifier = Modifier.height(20.dp))

                RulesCard()

                Spacer(modifier = Modifier.height(20.dp))

                ConfirmedPlayersCard(match = match)

                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }
}

@Composable
private fun MatchHeroCard(match: MockMatch) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF1EFEF)
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
                    .background(GreenPrimary.copy(alpha = 0.16f))
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
                        color = GreenPrimary
                    ) {
                        Text(
                            text = match.status,
                            color = Color(0xFFBDE8B9),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "\$1500 / jug",
                        color = GreenPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = match.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF202020)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "Ubicación",
                        tint = Color(0xFF4F574C),
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = match.location,
                        fontSize = 15.sp,
                        color = Color(0xFF4F574C)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DetailSmallBox(
                        modifier = Modifier.weight(1f),
                        icon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Fecha",
                                tint = GreenPrimary,
                                modifier = Modifier.size(26.dp)
                            )
                        },
                        text = match.date
                    )

                    DetailSmallBox(
                        modifier = Modifier.weight(1f),
                        icon = {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "Hora",
                                tint = GreenPrimary,
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
    icon: @Composable () -> Unit,
    text: String
) {
    Surface(
        modifier = modifier.height(68.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFE7E4E4)
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
                color = Color(0xFF202020),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun OrganizerCard() {
    DetailSectionCard {
        Text(
            text = "Organizador",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF202020)
        )

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(color = Color(0xFFE0E0E0))

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF233026))
                    .border(2.dp, GreenPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Organizador",
                    tint = White,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = "Martín G.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF202020)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "★",
                        color = GreenPrimary,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "4.8 (12 partidos)",
                        color = Color(0xFF4F574C),
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun MeetingDetailsCard(match: MockMatch) {
    DetailSectionCard {
        Text(
            text = "Detalles del Encuentro",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF202020)
        )

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(color = Color(0xFFE0E0E0))

        Spacer(modifier = Modifier.height(16.dp))

        DetailInfoRow(
            icon = {
                SurfaceGrassIcon(
                    modifier = Modifier.size(28.dp),
                    color = GreenPrimary
                )
            },
            title = "Superficie",
            description = match.tags.firstOrNull() ?: "Césped Sintético"
        )

        Spacer(modifier = Modifier.height(16.dp))

        DetailInfoRow(
            icon = {
                Text(
                    text = "♙",
                    fontSize = 24.sp,
                    color = GreenPrimary
                )
            },
            title = "Nivel sugerido",
            description = match.level
        )

        Spacer(modifier = Modifier.height(16.dp))

        DetailInfoRow(
            icon = {
                Text(
                    text = "⚽",
                    fontSize = 24.sp,
                    color = GreenPrimary
                )
            },
            title = "Incluye",
            description = "Pelota, pecheras y bebidas hidratantes\n(opcional)"
        )
    }
}

@Composable
private fun DetailInfoRow(
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
                color = Color(0xFF202020)
            )

            Text(
                text = description,
                fontSize = 15.sp,
                color = Color(0xFF4F574C),
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun RulesCard() {
    DetailSectionCard {
        Text(
            text = "Reglas",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF202020)
        )

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(color = Color(0xFFE0E0E0))

        Spacer(modifier = Modifier.height(16.dp))

        RuleItem("Llegar 15 min antes.")
        RuleItem("Confirmación obligatoria 24hs antes.")
        RuleItem("Se juega con lluvia leve, se suspende por\ntormenta.")
        RuleItem("Buena onda, cero mala leche.")
    }
}

@Composable
private fun RuleItem(text: String) {
    Row(
        modifier = Modifier.padding(bottom = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "•",
            color = GreenPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(18.dp)
        )

        Text(
            text = text,
            fontSize = 15.sp,
            color = Color(0xFF4F574C),
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun ConfirmedPlayersCard(match: MockMatch) {
    val currentPlayers = getCurrentPlayers(match.players)
    val maxPlayers = getMaxPlayers(match.players)

    DetailSectionCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Jugadores Confirmados",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF202020),
                modifier = Modifier.weight(1f)
            )

            Surface(
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFFE4E4E4)
            ) {
                Text(
                    text = "$currentPlayers / $maxPlayers",
                    color = GreenPrimary,
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
                PlayerSlot(name = "Martín", active = true, organizer = true)
                PlayerSlot(name = "Leo M.", active = true)
                PlayerSlot(name = "Alejandro", active = true, letter = "A")
                PlayerSlot(name = "Confirmado", active = true)
                PlayerSlot(name = "Confirmado", active = true)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PlayerSlot(name = "Confirmado", active = true)
                PlayerSlot(name = "Confirmado", active = true)
                PlayerSlot(name = "Confirmado", active = true)
                PlayerSlot(name = "Libre", active = false)
                PlayerSlot(name = "Libre", active = false)
            }
        }
    }
}

@Composable
private fun PlayerSlot(
    name: String,
    active: Boolean,
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
                    if (active) Color(0xFFE9E7E7) else Color.Transparent
                )
                .then(
                    if (organizer) {
                        Modifier.border(2.dp, GreenPrimary, CircleShape)
                    } else if (!active) {
                        Modifier.border(1.dp, Color(0xFFBFC6BC), CircleShape)
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
                        tint = GreenPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                letter != null -> {
                    Text(
                        text = letter,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6C6C6C)
                    )
                }

                active -> {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = name,
                        tint = Color(0xFF777777),
                        modifier = Modifier.size(20.dp)
                    )
                }

                else -> {
                    Text(
                        text = "+",
                        fontSize = 20.sp,
                        color = Color(0xFFBFC6BC)
                    )
                }
            }

            if (organizer) {
                Text(
                    text = "★",
                    color = GreenPrimary,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .background(Color(0xFFE9E7E7), CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = name,
            fontSize = 12.sp,
            fontWeight = if (organizer) FontWeight.Bold else FontWeight.Normal,
            color = if (active) {
                if (organizer) GreenPrimary else Color(0xFF4F574C)
            } else {
                Color(0xFFBFC6BC)
            },
            maxLines = 1
        )
    }
}

@Composable
private fun DetailSectionCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF1EFEF)
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
private fun BottomJoinButton(
    onJoinClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFFAF9F8),
        shadowElevation = 8.dp
    ) {
        Button(
            onClick = onJoinClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 10.dp,
                    bottom = 28.dp
                )
                .height(52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GreenPrimary,
                contentColor = White
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
            quadraticBezierTo(
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
            quadraticBezierTo(
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
            quadraticBezierTo(
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