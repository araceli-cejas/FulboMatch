package com.matchball.fulbomatch.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.matchball.fulbomatch.data.model.Notification
import com.matchball.fulbomatch.ui.components.MoonIconButton
import com.matchball.fulbomatch.ui.notification.NotificationViewModel
import com.matchball.fulbomatch.ui.theme.GreenPrimary

data class NotificationColors(
    val background: Color,
    val cardBackground: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val headerIcon: Color,
    val icon: Color,
    val accent: Color,
    val divider: Color,
    val border: Color,
    val bottomBarBackground: Color,
    val bottomIcon: Color,
    val unreadBadge: Color
)

private fun notificationColors(isDarkMode: Boolean): NotificationColors {
    return if (isDarkMode) {
        NotificationColors(
            background = Color(0xFF111111),
            cardBackground = Color(0xFF1A1A1A),
            textPrimary = Color(0xFFEDEDED),
            textSecondary = Color(0xFFBDBDBD),
            textMuted = Color(0xFF9E9E9E),
            headerIcon = Color(0xFFC9D1C9),
            icon = Color(0xFFC9D1C9),
            accent = Color(0xFF9EF49B),
            divider = Color(0xFF2A2A2A),
            border = Color(0xFF555555),
            bottomBarBackground = Color(0xFF151515),
            bottomIcon = Color(0xFFC9D1C9),
            unreadBadge = Color(0xFF9EF49B)
        )
    } else {
        NotificationColors(
            background = Color(0xFFF6F8FA),
            cardBackground = Color.White,
            textPrimary = Color(0xFF003311),
            textSecondary = Color(0xFF4F574C),
            textMuted = Color(0xFF6D6D6D),
            headerIcon = GreenPrimary,
            icon = Color(0xFF6D6D6D),
            accent = GreenPrimary,
            divider = Color(0xFFE0E0E0),
            border = Color(0xFFB9B9B9),
            bottomBarBackground = Color(0xFFF3F1F1),
            bottomIcon = Color(0xFF4F574C),
            unreadBadge = GreenPrimary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onMatchesClick: () -> Unit,
    onCreateClick: () -> Unit,
    onProfileClick: () -> Unit,
    viewModel: NotificationViewModel = viewModel()
) {
    val notifications by viewModel.notifications.collectAsState()
    val colors = notificationColors(isDarkMode)
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    LaunchedEffect(userId) {
        if (userId.isNotEmpty()) {
            viewModel.listenToNotifications(userId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notificaciones", fontWeight = FontWeight.Bold, color = colors.textPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = colors.headerIcon)
                    }
                },
                actions = {
                    MoonIconButton(isDarkMode, colors.headerIcon, onToggleDarkMode)
                    if (notifications.any { !it.isRead }) {
                        TextButton(onClick = { viewModel.markAllAsRead(userId) }) {
                            Text("Leer todas", color = colors.accent, fontWeight = FontWeight.Medium)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.background)
            )
        },
        bottomBar = {
            NotificationsBottomBar(colors, onHomeClick, onMatchesClick, onCreateClick, onProfileClick)
        },
        containerColor = colors.background
    ) { padding ->
        if (notifications.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Notifications, null, modifier = Modifier.size(64.dp), tint = colors.textMuted)
                    Spacer(Modifier.height(16.dp))
                    Text("No tienes notificaciones", color = colors.textSecondary, fontSize = 16.sp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notifications) { notification ->
                    NotificationCard(
                        notification = notification,
                        onClick = { viewModel.markAsRead(notification.id) },
                        colors = colors,
                        isDarkMode = isDarkMode
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationCard(
    notification: Notification,
    onClick: () -> Unit,
    colors: NotificationColors,
    isDarkMode: Boolean
) {
    val icon = when (notification.type) {
        "match_invite" -> Icons.Default.SportsSoccer
        "match_cancelled" -> Icons.Default.Cancel
        "match_reminder" -> Icons.Default.Event
        else -> Icons.Default.Notifications
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
        elevation = CardDefaults.cardElevation(if (notification.isRead) 1.dp else 4.dp),
        border = if (!notification.isRead) BorderStroke(1.dp, colors.accent.copy(alpha = 0.5f)) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape).background(if (notification.isRead) colors.background else colors.accent.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = if (notification.isRead) colors.textMuted else colors.accent)
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = notification.title,
                        fontWeight = if (notification.isRead) FontWeight.Medium else FontWeight.Bold,
                        color = colors.textPrimary,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    if (!notification.isRead) {
                        Box(Modifier.size(8.dp).clip(CircleShape).background(colors.unreadBadge))
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = notification.message,
                    color = colors.textSecondary,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun NotificationsBottomBar(
    colors: NotificationColors,
    onHomeClick: () -> Unit,
    onMatchesClick: () -> Unit,
    onCreateClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxWidth(), color = colors.bottomBarBackground, shadowElevation = 8.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 22.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomItem(Icons.Default.Home, "Inicio", false, colors, onHomeClick)
            BottomItem("⚽", "Partidos", false, colors, onMatchesClick)
            BottomItem(Icons.Default.Add, "Crear", false, colors, onCreateClick)
            BottomItem(Icons.Default.Person, "Perfil", false, colors, onProfileClick)
        }
    }
}

@Composable
private fun BottomItem(icon: Any, label: String, selected: Boolean, colors: NotificationColors, onClick: () -> Unit) {
    Column(modifier = Modifier.width(72.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(22.dp),
            color = if (selected) colors.accent else Color.Transparent,
            modifier = Modifier.height(44.dp).width(if (selected) 64.dp else 44.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (icon is ImageVector) Icon(icon, null, tint = if (selected) Color.Black else colors.bottomIcon)
                else Text(icon.toString(), fontSize = 20.sp)
            }
        }
        Text(label, fontSize = 11.sp, color = colors.bottomIcon)
    }
}
