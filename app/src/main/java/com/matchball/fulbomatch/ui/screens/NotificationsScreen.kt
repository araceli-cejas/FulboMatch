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
import com.matchball.fulbomatch.ui.components.MoonIconButton
import com.matchball.fulbomatch.ui.theme.GreenPrimary
import androidx.compose.foundation.clickable

private data class NotificationsColors(
    val background: Color,
    val cardBackground: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val headerIcon: Color,
    val accent: Color,
    val accentText: Color,
    val dangerRed: Color,
    val dangerSoft: Color,
    val warningOrange: Color,
    val warningDark: Color,
    val bottomBarBackground: Color,
    val bottomIcon: Color
)

private fun notificationsColors(isDarkMode: Boolean): NotificationsColors {
    return if (isDarkMode) {
        NotificationsColors(
            background = Color(0xFF111111),
            cardBackground = Color(0xFF1A1A1A),
            textPrimary = Color(0xFFEDEDED),
            textSecondary = Color(0xFFBDBDBD),
            headerIcon = Color(0xFFC9D1C9),
            accent = Color(0xFF9EF49B),
            accentText = Color(0xFF111111),
            dangerRed = Color(0xFFFF6B6B),
            dangerSoft = Color(0xFF3A1F1F),
            warningOrange = Color(0xFFFFB86C),
            warningDark = Color(0xFF111111),
            bottomBarBackground = Color(0xFF151515),
            bottomIcon = Color(0xFFC9D1C9)
        )
    } else {
        NotificationsColors(
            background = Color(0xFFFAF9F8),
            cardBackground = Color.White,
            textPrimary = Color(0xFF202020),
            textSecondary = Color(0xFF4F574C),
            headerIcon = GreenPrimary,
            accent = GreenPrimary,
            accentText = Color.White,
            dangerRed = Color(0xFFC62828),
            dangerSoft = Color(0xFFFFD6D6),
            warningOrange = Color(0xFFFFB86C),
            warningDark = Color(0xFF8A5A14),
            bottomBarBackground = Color(0xFFFAF9F8),
            bottomIcon = Color(0xFF4F574C)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onMatchesClick: () -> Unit,
    onCreateClick: () -> Unit,
    onProfileClick: () -> Unit,
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {}
) {
    val colors = notificationsColors(isDarkMode)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notificaciones",
                        color = colors.textPrimary,
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold
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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.background
                )
            )
        },
        bottomBar = {
            NotificationsBottomBar(
                colors = colors,
                onHomeClick = onHomeClick,
                onMatchesClick = onMatchesClick,
                onCreateClick = onCreateClick,
                onProfileClick = onProfileClick
            )
        },
        containerColor = colors.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
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
                iconTint = colors.accentText,
                iconBackground = colors.accent,
                colors = colors
            )

            NotificationCard(
                title = "Nuevo jugador anotado",
                description = "Martín se sumó a tu partido.",
                time = "Hace 2 min",
                icon = Icons.Default.PersonAdd,
                iconTint = colors.accentText,
                iconBackground = colors.accent,
                colors = colors
            )

            NotificationCard(
                title = "Partido cancelado",
                description = "El organizador canceló el partido del sábado.",
                time = "Hace 1 h",
                icon = Icons.Default.EventBusy,
                iconTint = colors.dangerRed,
                iconBackground = colors.dangerSoft,
                colors = colors
            )

            NotificationCard(
                title = "Jugador dado de baja",
                description = "Un jugador se bajó de tu partido del domingo.",
                time = "Hace 1 h",
                icon = Icons.Default.PersonRemove,
                iconTint = colors.warningDark,
                iconBackground = colors.warningOrange,
                colors = colors
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
    iconBackground: Color,
    colors: NotificationsColors
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(11.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.cardBackground
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
                    color = colors.textPrimary,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = description,
                    fontSize = 15.sp,
                    color = colors.textSecondary,
                    lineHeight = 21.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = time,
                fontSize = 12.sp,
                color = colors.textSecondary
            )
        }
    }
}

@Composable
private fun NotificationsBottomBar(
    colors: NotificationsColors,
    onHomeClick: () -> Unit,
    onMatchesClick: () -> Unit,
    onCreateClick: () -> Unit,
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
                onClick = onCreateClick
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
private fun UnselectedBottomItem(
    icon: ImageVector,
    label: String,
    colors: NotificationsColors,
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
    colors: NotificationsColors,
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