package com.matchball.fulbomatch.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.SportsSoccer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.matchball.fulbomatch.ui.theme.GreenPrimary


private val NotificationsBackground = Color(0xFFFAF9F8)
private val TextDark = Color(0xFF202020)
private val TextSoft = Color(0xFF4F574C)
private val DangerRed = Color(0xFFC62828)
private val DangerSoft = Color(0xFFFFD6D6)
private val WarningOrange = Color(0xFFFFB86C)
private val WarningDark = Color(0xFF8A5A14)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onMatchesClick: () -> Unit,
    onCreateClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notificaciones",
                        color = GreenPrimary,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NotificationsBackground
                )
            )
        },
        bottomBar = {
            NotificationsBottomBar(
                onHomeClick = onHomeClick,
                onMatchesClick = onMatchesClick,
                onCreateClick = onCreateClick,
                onProfileClick = onProfileClick
            )
        },
        containerColor = NotificationsBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            NotificationCard(
                title = "Partido editado",
                description = "El organizador realizó cambios en el partido del domingo.",
                time = "Hace 2 min",
                icon = Icons.Default.EditCalendar,
                iconTint = Color.White,
                iconBackground = GreenPrimary
            )

            NotificationCard(
                title = "Nuevo jugador anotado",
                description = "Martín se sumó a tu partido.",
                time = "Hace 2 min",
                icon = Icons.Default.PersonAdd,
                iconTint = Color.White,
                iconBackground = GreenPrimary
            )

            NotificationCard(
                title = "Partido cancelado",
                description = "El organizador canceló el partido del sábado.",
                time = "Hace 1 h",
                icon = Icons.Default.EventBusy,
                iconTint = DangerRed,
                iconBackground = DangerSoft
            )

            NotificationCard(
                title = "Jugador dado de baja",
                description = "Un jugador se bajó de tu partido del domingo.",
                time = "Hace 1 h",
                icon = Icons.Default.PersonRemove,
                iconTint = WarningDark,
                iconBackground = WarningOrange
            )
        }
    }
}

@Composable
private fun NotificationCard(
    title: String,
    description: String,
    time: String,
    icon: ImageVector,
    iconTint: Color,
    iconBackground: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(11.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(49.dp)
                    .clip(CircleShape)
                    .background(iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(25.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = description,
                    fontSize = 15.sp,
                    color = TextSoft,
                    lineHeight = 21.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = time,
                fontSize = 12.sp,
                color = TextSoft
            )
        }
    }
}

@Composable
private fun NotificationsBottomBar(
    onHomeClick: () -> Unit,
    onMatchesClick: () -> Unit,
    onCreateClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = NotificationsBackground,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomItem(
                icon = Icons.Default.Home,
                label = "Inicio",
                selected = false,
                onClick = onHomeClick
            )

            BottomItem(
                icon = Icons.Default.SportsSoccer,
                label = "Partidos",
                selected = false,
                onClick = onMatchesClick
            )

            BottomItem(
                icon = Icons.Default.Add,
                label = "Crear",
                selected = false,
                onClick = onCreateClick
            )

            BottomItem(
                icon = Icons.Default.Person,
                label = "Perfil",
                selected = false,
                onClick = onProfileClick
            )
        }
    }
}

@Composable
private fun BottomItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.width(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(28.dp),
            color = if (selected) GreenPrimary else Color.Transparent,
            modifier = Modifier
                .height(38.dp)
                .width(if (selected) 70.dp else 44.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (selected) Color.White else TextSoft,
                    modifier = Modifier.size(23.dp)
                )
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