package com.matchball.fulbomatch.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.matchball.fulbomatch.R
import com.matchball.fulbomatch.data.model.UserProfile
import com.matchball.fulbomatch.ui.theme.GreenPrimary
import com.matchball.fulbomatch.ui.theme.White
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit = {},
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onMatchesClick: () -> Unit = {},
    onCreateMatchClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    viewModel: com.matchball.fulbomatch.ui.profile.UserViewModel = viewModel()
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val context = LocalContext.current
    val colors = editProfileColors(isDarkMode)

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var zone by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) profileBitmap = bitmap
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) cameraLauncher.launch()
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(context).lastLocation.addOnSuccessListener { loc ->
                loc?.let {
                    try {
                        val geocoder = android.location.Geocoder(context, java.util.Locale.getDefault())
                        @Suppress("DEPRECATION")
                        val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                        if (!addresses.isNullOrEmpty()) {
                            val address = addresses[0]
                            zone = when {
                                // Si tiene barrio/sublocality, usá eso
                                !address.subLocality.isNullOrBlank() -> address.subLocality
                                // Si no, usá la localidad (ciudad/municipio)
                                !address.locality.isNullOrBlank() -> address.locality
                                // Si no, usá la calle y número
                                !address.thoroughfare.isNullOrBlank() -> {
                                    val numero = address.subThoroughfare ?: ""
                                    "${address.thoroughfare} $numero".trim()
                                }
                                // Fallback: las coordenadas como antes
                                else -> "${it.latitude.toString().take(7)}, ${it.longitude.toString().take(7)}"
                            }
                        }
                    } catch (e: Exception) {
                        // Si Geocoder falla (sin internet), muestra coordenadas
                        zone = "${it.latitude.toString().take(7)}, ${it.longitude.toString().take(7)}"
                    }
                }
            }
        }
    }
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = colors.textPrimary,
        unfocusedTextColor = colors.textPrimary,
        focusedContainerColor = colors.inputBackground,
        unfocusedContainerColor = colors.inputBackground,
        focusedBorderColor = GreenPrimary,
        unfocusedBorderColor = colors.border,
        focusedLabelColor = GreenPrimary,
        unfocusedLabelColor = colors.textSecondary,
        disabledBorderColor = colors.border,
        disabledTextColor = colors.textSecondary,
        disabledLabelColor = colors.textSecondary,
        disabledContainerColor = colors.inputBackground
    )

    LaunchedEffect(userProfile) {
        userProfile?.let {
            fullName = it.nombre; email = it.email; phone = it.phone
            age = it.age; zone = it.zone; description = it.description
            
            if (it.photoBase64.isNotEmpty() && profileBitmap == null) {
                try {
                    val imageBytes = Base64.decode(it.photoBase64, Base64.DEFAULT)
                    profileBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                } catch (e: Exception) {
                    // Si falla, se queda nulo
                }
            }
        }
    }

    Scaffold(containerColor = colors.background) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) { 
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack, 
                        "Volver", 
                        tint = colors.headerIcon
                    ) 
                }
                Text(
                    "Editar perfil", 
                    fontSize = 20.sp, 
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
            }

            Box(modifier = Modifier.align(Alignment.CenterHorizontally).size(112.dp), contentAlignment = Alignment.BottomEnd) {
                if (profileBitmap != null) {
                    Image(bitmap = profileBitmap!!.asImageBitmap(), "Foto", modifier = Modifier.size(112.dp).clip(CircleShape), contentScale = ContentScale.Crop)
                } else {
                    Box(modifier = Modifier.size(112.dp).clip(CircleShape).background(colors.avatarPlaceholder), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, "Sin foto", tint = colors.textSecondary, modifier = Modifier.size(56.dp))
                    }
                }
                Box(modifier = Modifier.size(34.dp).clip(CircleShape).background(GreenPrimary).clickable {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }, contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Edit, "Editar", tint = White, modifier = Modifier.size(18.dp))
                }
            }

            OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), enabled = false, colors = textFieldColors) 
            OutlinedTextField(value = phone, onValueChange = { if (it.all { char -> char.isDigit() }) phone = it }, label = { Text("Teléfono (Solo números)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), colors = textFieldColors)
            OutlinedTextField(value = age, onValueChange = { if (it.all { char -> char.isDigit() }) age = it }, label = { Text("Edad") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), colors = textFieldColors)
            OutlinedTextField(value = zone, onValueChange = { zone = it }, label = { Text("Zona") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)

            TextButton(onClick = { locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }) {
                Icon(Icons.Default.Place, null, tint = colors.accent)
                Spacer(Modifier.width(8.dp))
                Text("Usar mi ubicación actual", color = colors.accent)
            }

            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth().height(100.dp), colors = textFieldColors)

            Button(
                onClick = {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {
                        val base64Image = profileBitmap?.let { bitmap ->
                            val outputStream = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
                            Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
                        } ?: userProfile?.photoBase64 ?: ""

                        viewModel.updateProfile(
                            UserProfile(
                                id = userId,
                                nombre = fullName,
                                email = email,
                                phone = phone,
                                age = age,
                                zone = zone,
                                posicion = userProfile?.posicion ?: "",
                                description = description,
                                photoBase64 = base64Image
                            )
                        )
                        onSaveClick()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Text("Guardar Cambios", color = White)
            }
        }
    }
}

private data class EditProfileColors(
    val background: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val border: Color,
    val inputBackground: Color,
    val headerIcon: Color,
    val avatarPlaceholder: Color,
    val accent: Color
)

private fun editProfileColors(isDarkMode: Boolean): EditProfileColors {
    return if (isDarkMode) {
        EditProfileColors(
            background = Color(0xFF111111),
            textPrimary = Color(0xFFEDEDED),
            textSecondary = Color(0xFFBDBDBD),
            border = Color(0xFF3E463E),
            inputBackground = Color(0xFF1A1A1A),
            headerIcon = Color(0xFFC9D1C9),
            avatarPlaceholder = Color(0xFF2A2A2A),
            accent = Color(0xFF9EF49B)
        )
    } else {
        EditProfileColors(
            background = Color(0xFFF6F8FA),
            textPrimary = Color(0xFF202020),
            textSecondary = Color(0xFF555555),
            border = Color(0xFFD0D0D0),
            inputBackground = Color.White,
            headerIcon = GreenPrimary,
            avatarPlaceholder = Color(0xFFE0E0E0),
            accent = GreenPrimary
        )
    }
}