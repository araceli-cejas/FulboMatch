package com.matchball.fulbomatch.ui.screens

import android.util.Log
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.matchball.fulbomatch.data.model.Notification
import com.matchball.fulbomatch.ui.notification.NotificationViewModel
import com.matchball.fulbomatch.ui.theme.GreenPrimary
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {},
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit = {},
    onMatchesClick: () -> Unit = {},
    onCreateClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    viewModel: NotificationViewModel = viewModel()
) {
    val notifications by viewModel.notifications.collectAsState()
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    Log.d("FIREBASE_NOTIS", "Mi UID actual es: $currentUserId")

    // Escuchamos a Firebase apenas entramos a la pantalla
    LaunchedEffect(currentUserId) {
        currentUserId?.let { viewModel.listenToNotifications(it) }
    }

    Scaffold(
        containerColor = Color(0xFFF6F8FA)
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth().height(64.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = GreenPrimary)
                }
                Text("Notificaciones", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.weight(1f))
            }

            // Lista real conectada a Firebase
            if (notifications.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No tienes notificaciones", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    items(notifications) { notif ->
                        NotificationCard(notif) {
                            if (!notif.isRead) viewModel.markAsRead(notif.id)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationCard(notification: Notification, onClick: () -> Unit) {
    // Configuración visual según el 'type' de la notificación
    val (icon, bgColor, iconTint) = when (notification.type) {
        "edit" -> Triple(Icons.Default.EditCalendar, Color(0xFFE8F5E9), Color(0xFF2E7D32)) // Verde oscuro
        "new_player" -> Triple(Icons.Default.PersonAdd, Color(0xFF2E7D32), Color.White) // Círculo verde
        "cancel" -> Triple(Icons.Default.EventBusy, Color(0xFFFFEBEE), Color(0xFFC62828)) // Rojo clarito
        "leave" -> Triple(Icons.Default.PersonRemove, Color(0xFFFFF3E0), Color(0xFFEF6C00)) // Naranja clarito
        else -> Triple(Icons.Default.Notifications, Color.LightGray, Color.Black)
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape).background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(notification.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                    // Transformación del Timestamp a texto formateado en tiempo real
                    Text(formatTimestamp(notification.timestamp), fontSize = 12.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(notification.message, color = Color.DarkGray, fontSize = 14.sp)
            }
        }
    }
}

// Función auxiliar para transformar el Timestamp en una fecha legible
private fun formatTimestamp(timestamp: Timestamp): String {
    val formatter = SimpleDateFormat("dd MMM, HH:mm", Locale("es", "ES"))
    return formatter.format(timestamp.toDate())
}