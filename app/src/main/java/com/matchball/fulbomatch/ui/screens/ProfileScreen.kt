package com.matchball.fulbomatch.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.matchball.fulbomatch.R
import com.matchball.fulbomatch.ui.components.MoonIconButton
import com.matchball.fulbomatch.ui.theme.GreenPrimary

private data class ProfileColors(
    val background: Color,
    val cardBackground: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val headerIcon: Color,
    val icon: Color,
    val accent: Color,
    val accentText: Color,
    val chipBackground: Color,
    val avatarBackground: Color,
    val circleIconBackground: Color,
    val divider: Color,
    val logoutBackground: Color,
    val logoutIconBackground: Color,
    val danger: Color,
    val bottomBarBackground: Color,
    val bottomIcon: Color,
    val selectedBottomIcon: Color
)

private fun profileColors(isDarkMode: Boolean): ProfileColors {
    return if (isDarkMode) {
        ProfileColors(
            background = Color(0xFF111111),
            cardBackground = Color(0xFF1A1A1A),
            textPrimary = Color(0xFFEDEDED),
            textSecondary = Color(0xFFBDBDBD),
            textMuted = Color(0xFF9E9E9E),
            headerIcon = Color(0xFFC9D1C9),
            icon = Color(0xFFC9D1C9),
            accent = Color(0xFF9EF49B),
            accentText = Color(0xFF111111),
            chipBackground = Color(0xFF2A2A2A),
            avatarBackground = Color(0xFF22302B),
            circleIconBackground = Color(0xFF2A2A2A),
            divider = Color(0xFF2A2A2A),
            logoutBackground = Color(0xFF3A1F1F),
            logoutIconBackground = Color(0xFF4A2A2A),
            danger = Color(0xFFFF6B6B),
            bottomBarBackground = Color(0xFF151515),
            bottomIcon = Color(0xFFC9D1C9),
            selectedBottomIcon = Color(0xFF111111)
        )
    } else {
        ProfileColors(
            background = Color(0xFFF6F6F6),
            cardBackground = Color.White,
            textPrimary = Color(0xFF202020),
            textSecondary = Color(0xFF444444),
            textMuted = Color(0xFF333333),
            headerIcon = GreenPrimary,
            icon = Color(0xFF6A6F66),
            accent = GreenPrimary,
            accentText = Color(0xFFBDE8B9),
            chipBackground = Color(0xFFEDEDED),
            avatarBackground = Color(0xFFEDEDED),
            circleIconBackground = Color(0xFFE4E4E4),
            divider = Color(0xFFE0E0E0),
            logoutBackground = Color(0xFFFFD6D6),
            logoutIconBackground = Color(0xFFFFEEEE),
            danger = Color(0xFFC62828),
            bottomBarBackground = Color(0xFFF3F1F1),
            bottomIcon = Color(0xFF4F574C),
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
    onToggleDarkMode: () -> Unit = {}
) {
    val colors = profileColors(isDarkMode)

    Scaffold(
        bottomBar = {
            ProfileBottomBar(
                colors = colors,
                onHomeClick = onHomeClick,
                onMatchesClick = onMatchesClick,
                onCreateMatchClick = onCreateMatchClick
            )
        },
        containerColor = colors.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(colors.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 12.dp)
                .semantics { contentDescription = "Pantalla de perfil de usuario" },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileHeader(
                colors = colors,
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode,
                onNotificationsClick = onNotificationsClick
            )

            Spacer(modifier = Modifier.height(20.dp))

            ProfileAvatar(colors = colors)

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Lucas Fernández",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            ProfileUserInfo(colors = colors)

            Spacer(modifier = Modifier.height(30.dp))

            ProfileStatsRow(colors = colors)

            Spacer(modifier = Modifier.height(28.dp))

            ProfileOptionCard(
                icon = Icons.Default.Person,
                title = "Editar perfil",
                colors = colors,
                onClick = onEditProfileClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            ProfileOptionCard(
                iconText = "⚽",
                title = "Mis partidos",
                colors = colors,
                onClick = { onMatchesClick() }
            )

            Spacer(modifier = Modifier.height(18.dp))

            HorizontalDivider(
                color = colors.divider,
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(18.dp))

            LogoutButton(
                colors = colors,
                onClick = onLogoutClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Eliminar cuenta",
                color = colors.danger,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable {
                    onDeleteAccountClick()
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ProfileHeader(
    colors: ProfileColors,
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
            .background(colors.background),
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
private fun ProfileAvatar(
    colors: ProfileColors
) {
    Surface(
        modifier = Modifier.size(112.dp),
        shape = CircleShape,
        color = colors.avatarBackground,
        shadowElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(colors.accent.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Foto de perfil",
                tint = colors.accent,
                modifier = Modifier.size(58.dp)
            )
        }
    }
}

@Composable
private fun ProfileUserInfo(
    colors: ProfileColors
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = colors.chipBackground
        ) {
            Text(
                text = "Defensor",
                fontSize = 13.sp,
                color = colors.textPrimary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        Text(
            text = "  •  24 años  •  ",
            fontSize = 15.sp,
            color = colors.textSecondary
        )

        Icon(
            imageVector = Icons.Default.Place,
            contentDescription = "Ubicación",
            tint = colors.textSecondary,
            modifier = Modifier.size(17.dp)
        )

        Text(
            text = "Caballito",
            fontSize = 15.sp,
            color = colors.textSecondary
        )
    }
}

@Composable
private fun ProfileStatsRow(
    colors: ProfileColors
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ProfileStatCard(
            number = "12",
            label = "PARTIDOS",
            colors = colors,
            modifier = Modifier.weight(1f)
        )

        ProfileStatCard(
            number = "5",
            label = "CREADOS",
            colors = colors,
            modifier = Modifier.weight(1f)
        )

        ProfileStatCard(
            number = "7",
            label = "ANOTADOS",
            colors = colors,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ProfileStatCard(
    number: String,
    label: String,
    colors: ProfileColors,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(76.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = number,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = colors.accent
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = colors.textSecondary
            )
        }
    }
}

@Composable
private fun ProfileOptionCard(
    icon: ImageVector,
    title: String,
    colors: ProfileColors,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleIconContainer(colors = colors) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = colors.icon,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                fontSize = 17.sp,
                color = colors.textPrimary,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                contentDescription = "Ir a $title",
                tint = colors.textSecondary
            )
        }
    }
}

@Composable
private fun ProfileOptionCard(
    iconText: String,
    title: String,
    colors: ProfileColors,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleIconContainer(colors = colors) {
                Text(
                    text = iconText,
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                fontSize = 17.sp,
                color = colors.textPrimary,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                contentDescription = "Ir a $title",
                tint = colors.textSecondary
            )
        }
    }
}

@Composable
private fun CircleIconContainer(
    colors: ProfileColors,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(colors.circleIconBackground),
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
private fun LogoutButton(
    colors: ProfileColors,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = colors.logoutBackground
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(colors.logoutIconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Cerrar sesión",
                    tint = colors.danger,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Cerrar sesión",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.danger
            )
        }
    }
}

@Composable
private fun ProfileBottomBar(
    colors: ProfileColors,
    onHomeClick: () -> Unit,
    onMatchesClick: () -> Unit,
    onCreateMatchClick: () -> Unit
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
                onClick = onCreateMatchClick
            )

            SelectedBottomItem(
                icon = Icons.Default.Person,
                label = "Perfil",
                colors = colors,
                onClick = { }
            )
        }
    }
}

@Composable
private fun SelectedBottomItem(
    icon: ImageVector,
    label: String,
    colors: ProfileColors,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(76.dp)
            .height(66.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(colors.accent)
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
                tint = colors.selectedBottomIcon,
                modifier = Modifier.size(26.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = label,
                color = colors.selectedBottomIcon,
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
    colors: ProfileColors,
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
    colors: ProfileColors,
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