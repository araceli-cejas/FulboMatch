package com.matchball.fulbomatch.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.LocationServices
import com.matchball.fulbomatch.R
import com.matchball.fulbomatch.ui.components.MoonIconButton
import com.matchball.fulbomatch.ui.partido.PartidoViewModel
import com.matchball.fulbomatch.ui.theme.GreenPrimary

// --- CLASES DE ESTILO ---
private data class ProfileColors(
    val background: Color, val cardBackground: Color, val textPrimary: Color, val textSecondary: Color,
    val textMuted: Color, val headerIcon: Color, val icon: Color, val accent: Color, val accentText: Color,
    val chipBackground: Color, val avatarBackground: Color, val circleIconBackground: Color, val divider: Color,
    val logoutBackground: Color, val logoutIconBackground: Color, val danger: Color, val bottomBarBackground: Color,
    val bottomIcon: Color, val selectedBottomIcon: Color
)

private fun profileColors(isDarkMode: Boolean): ProfileColors {
    return if (isDarkMode) {
        ProfileColors(
            background = Color(0xFF111111), cardBackground = Color(0xFF1A1A1A), textPrimary = Color(0xFFEDEDED),
            textSecondary = Color(0xFFBDBDBD), textMuted = Color(0xFF9E9E9E), headerIcon = Color(0xFFC9D1C9),
            icon = Color(0xFFC9D1C9), accent = Color(0xFF9EF49B), accentText = Color(0xFF111111),
            chipBackground = Color(0xFF2A2A2A), avatarBackground = Color(0xFF22302B), circleIconBackground = Color(0xFF2A2A2A),
            divider = Color(0xFF2A2A2A), logoutBackground = Color(0xFF3A1F1F), logoutIconBackground = Color(0xFF4A2A2A),
            danger = Color(0xFFFF6B6B), bottomBarBackground = Color(0xFF151515), bottomIcon = Color(0xFFC9D1C9),
            selectedBottomIcon = Color(0xFF111111)
        )
    } else {
        ProfileColors(
            background = Color(0xFFF6F6F6), cardBackground = Color.White, textPrimary = Color(0xFF202020),
            textSecondary = Color(0xFF444444), textMuted = Color(0xFF333333), headerIcon = GreenPrimary,
            icon = Color(0xFF6A6F66), accent = GreenPrimary, accentText = Color.White,
            chipBackground = Color(0xFFEDEDED), avatarBackground = Color(0xFFEDEDED), circleIconBackground = Color(0xFFE4E4E4),
            divider = Color(0xFFE0E0E0), logoutBackground = Color(0xFFFFD6D6), logoutIconBackground = Color(0xFFFFEEEE),
            danger = Color(0xFFC62828), bottomBarBackground = Color(0xFFF3F1F1), bottomIcon = Color(0xFF4F574C),
            selectedBottomIcon = Color(0xFFBDE8B9)
        )
    }
}

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit = onBackClick,
    onCreateMatchClick: () -> Unit = {},
    onMatchesClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onDeleteAccountClick: () -> Unit = {},
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {},
    viewModel: com.matchball.fulbomatch.ui.profile.UserViewModel = viewModel() ,
    partidoViewModel: PartidoViewModel = viewModel()
) {
    val colors = profileColors(isDarkMode)
    val userProfile by viewModel.userProfile.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val todosLosPartidos by partidoViewModel.partidos.collectAsState() // <--- USÁ ESTE
    val currentUserId = partidoViewModel.currentUserId

    val creadosCount = todosLosPartidos.count { it.creadorId == currentUserId }
    val anotadosCount = todosLosPartidos.count { it.jugadoresConfirmados.contains(currentUserId) && it.creadorId != currentUserId }
    val totalCount = creadosCount + anotadosCount

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.loadCurrentUserProfile()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                loc?.let { viewModel.updateLocation("${it.latitude.toString().take(6)}, ${it.longitude.toString().take(6)}") }
            }
        }
    }



    Scaffold(
        bottomBar = { ProfileBottomBar(colors, onHomeClick, onMatchesClick, onCreateMatchClick) },
        containerColor = colors.background
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileHeader(colors, isDarkMode, onToggleDarkMode, onNotificationsClick)
            Spacer(modifier = Modifier.height(20.dp))

            // SOLO UN AVATAR: Lógica dinámica de Base64
            // ... reemplaza la parte del Surface dentro de ProfileScreen ...

            Surface(modifier = Modifier.size(112.dp), shape = CircleShape, color = colors.avatarBackground) {
                // 1. Decodificamos el bitmap de forma segura con una variable local
                val bitmap = remember(userProfile?.photoBase64) {
                    val base64 = userProfile?.photoBase64
                    if (!base64.isNullOrEmpty()) {
                        try {
                            val imageBytes = Base64.decode(base64, Base64.DEFAULT)
                            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        } catch (e: Exception) {
                            null
                        }
                    } else {
                        null
                    }
                }

                // 2. Mostramos el resultado de la variable 'bitmap' (que ya es seguro)
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.Person,
                        "Avatar",
                        tint = colors.icon.copy(alpha = 0.5f),
                        modifier = Modifier.size(56.dp).padding(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            Text(userProfile?.nombre ?: "Usuario", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)

            TextButton(onClick = { locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }) {
                Text("Actualizar ubicación GPS", fontSize = 12.sp, color = colors.accent)
            }

            ProfileUserInfo(
                userProfile?.posicion?.takeIf { it != "Completar" },
                userProfile?.age ?: "--",
                userProfile?.zone ?: "Sin zona",
                colors = colors)
            Spacer(modifier = Modifier.height(30.dp))
            ProfileStatsRow(
                partidos = totalCount,
                creados = creadosCount,
                anotados = anotadosCount,
                colors = colors
            )

            Spacer(modifier = Modifier.height(28.dp))

            ProfileOptionCard(Icons.Default.Person, "Editar perfil", colors, onEditProfileClick)
            Spacer(modifier = Modifier.height(12.dp))
            ProfileOptionCard("⚽", "Mis partidos", colors, onMatchesClick)
            Spacer(modifier = Modifier.height(18.dp))
            LogoutButton(colors) { viewModel.logout(); onLogoutClick() }
        }
    }
}

// --- RESTO DE COMPONENTES SIN ProfileAvatar (ya eliminado) ---
@Composable
private fun ProfileUserInfo(posicion: String?, edad: String, zona: String, colors: ProfileColors) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        // POSICIÓN
        if (posicion != null) {
            Surface(shape = RoundedCornerShape(6.dp), color = colors.chipBackground) {
                Text(posicion, fontSize = 13.sp, color = colors.textPrimary, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
            }
            Text(" • ", fontSize = 15.sp, color = colors.textSecondary)
        }
        // EDAD
        Text("$edad años • ", fontSize = 15.sp, color = colors.textSecondary)
        // ZONA
        Icon(Icons.Default.Place, "Ubicación", tint = colors.textSecondary, modifier = Modifier.size(17.dp))
        Text(zona, fontSize = 15.sp, color = colors.textSecondary)
    }
}

@Composable
private fun ProfileHeader(colors: ProfileColors, isDarkMode: Boolean, onToggleDarkMode: () -> Unit, onNotificationsClick: () -> Unit) {
    val logoRes = if (isDarkMode) R.drawable.logo_dark else R.drawable.nombre
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Image(painterResource(logoRes), "Logo", modifier = Modifier.width(190.dp).height(42.dp), contentScale = ContentScale.Fit)
        Spacer(modifier = Modifier.weight(1f))
        MoonIconButton(isDarkMode, colors.headerIcon, onToggleDarkMode)
        IconButton(onClick = onNotificationsClick) { Icon(Icons.Default.Notifications, "Notis", tint = colors.headerIcon) }
    }
}

@Composable
private fun ProfileStatsRow(
    partidos: Int,
    creados: Int,
    anotados: Int,
    colors: ProfileColors
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        ProfileStatCard(partidos.toString(), "PARTIDOS", colors, Modifier.weight(1f))
        ProfileStatCard(creados.toString(), "CREADOS", colors, Modifier.weight(1f))
        ProfileStatCard(anotados.toString(), "ANOTADOS", colors, Modifier.weight(1f))
    }
}

@Composable
private fun ProfileStatCard(number: String, label: String, colors: ProfileColors, modifier: Modifier) {
    Card(modifier = modifier.height(76.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(colors.cardBackground)) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(number, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = colors.accent)
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = colors.textSecondary)
        }
    }
}

@Composable
private fun ProfileOptionCard(icon: ImageVector, title: String, colors: ProfileColors, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().height(66.dp).clickable { onClick() }, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(colors.cardBackground)) {
        Row(Modifier.fillMaxSize().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            CircleIconContainer(colors) { Icon(icon, title, tint = colors.icon, modifier = Modifier.size(22.dp)) }
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, fontSize = 17.sp, color = colors.textPrimary, modifier = Modifier.weight(1f))
            Icon(Icons.AutoMirrored.Filled.NavigateNext, "Ir", tint = colors.textSecondary)
        }
    }
}

@Composable
private fun ProfileOptionCard(iconText: String, title: String, colors: ProfileColors, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().height(66.dp).clickable { onClick() }, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(colors.cardBackground)) {
        Row(Modifier.fillMaxSize().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            CircleIconContainer(colors) { Text(iconText, fontSize = 22.sp) }
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, fontSize = 17.sp, color = colors.textPrimary, modifier = Modifier.weight(1f))
            Icon(Icons.AutoMirrored.Filled.NavigateNext, "Ir", tint = colors.textSecondary)
        }
    }
}

@Composable
private fun CircleIconContainer(colors: ProfileColors, content: @Composable BoxScope.() -> Unit) {
    Box(Modifier.size(42.dp).clip(CircleShape).background(colors.circleIconBackground), contentAlignment = Alignment.Center, content = content)
}

@Composable
private fun LogoutButton(colors: ProfileColors, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().height(66.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = colors.cardBackground) {
        Row(Modifier.fillMaxSize().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(42.dp).clip(CircleShape).background(colors.logoutIconBackground), contentAlignment = Alignment.Center) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, "Logout", tint = colors.danger, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text("Cerrar sesión", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = colors.danger)
        }
    }
}

@Composable
private fun ProfileBottomBar(colors: ProfileColors, onHomeClick: () -> Unit, onMatchesClick: () -> Unit, onCreateMatchClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth().padding(10.dp), shape = RoundedCornerShape(18.dp), color = colors.bottomBarBackground, tonalElevation = 4.dp) {
        Row(Modifier.fillMaxWidth().height(84.dp).padding(horizontal = 14.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            UnselectedBottomItem(Icons.Default.Home, "Inicio", colors, onHomeClick)
            UnselectedBottomItem("⚽", "Partidos", colors, onMatchesClick)
            UnselectedBottomItem(Icons.Default.Add, "Crear", colors, onCreateMatchClick)
            SelectedBottomItem(Icons.Default.Person, "Perfil", colors) { }
        }
    }
}

@Composable
private fun SelectedBottomItem(icon: ImageVector, label: String, colors: ProfileColors, onClick: () -> Unit) {
    Box(Modifier.width(76.dp).height(66.dp).clip(RoundedCornerShape(22.dp)).background(colors.accent).clickable { onClick() }, contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, label, tint = colors.selectedBottomIcon, modifier = Modifier.size(26.dp))
            Text(label, color = colors.selectedBottomIcon, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun UnselectedBottomItem(icon: ImageVector, label: String, colors: ProfileColors, onClick: () -> Unit) {
    Column(Modifier.width(64.dp).height(64.dp).clickable { onClick() }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(icon, label, tint = colors.bottomIcon, modifier = Modifier.size(26.dp))
        Text(label, color = colors.bottomIcon, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun UnselectedBottomItem(iconText: String, label: String, colors: ProfileColors, onClick: () -> Unit) {
    Column(Modifier.width(64.dp).height(64.dp).clickable { onClick() }, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(iconText, fontSize = 24.sp, color = colors.bottomIcon)
        Text(label, color = colors.bottomIcon, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}