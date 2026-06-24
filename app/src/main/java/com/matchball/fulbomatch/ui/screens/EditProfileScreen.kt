package com.matchball.fulbomatch.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
                loc?.let { zone = "${it.latitude.toString().take(6)}, ${it.longitude.toString().take(6)}" }
            }
        }
    }

    LaunchedEffect(userProfile) {
        userProfile?.let {
            fullName = it.nombre; email = it.email; phone = it.phone
            age = it.age; zone = it.zone; description = it.description
        }
    }

    Scaffold(containerColor = Color(0xFFF6F8FA)) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") }
                Text("Editar perfil", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Box(modifier = Modifier.align(Alignment.CenterHorizontally).size(112.dp), contentAlignment = Alignment.BottomEnd) {
                if (profileBitmap != null) {
                    Image(bitmap = profileBitmap!!.asImageBitmap(), "Foto", modifier = Modifier.size(112.dp).clip(CircleShape), contentScale = ContentScale.Crop)
                } else {
                    Image(painterResource(R.drawable.hombre), "Foto", modifier = Modifier.size(112.dp).clip(CircleShape), contentScale = ContentScale.Crop)
                }
                Box(modifier = Modifier.size(34.dp).clip(CircleShape).background(GreenPrimary).clickable {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }, contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Edit, "Editar", tint = White, modifier = Modifier.size(18.dp))
                }
            }

            OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Edad") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            OutlinedTextField(value = zone, onValueChange = { zone = it }, label = { Text("Zona") }, modifier = Modifier.fillMaxWidth())

            TextButton(onClick = { locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) }) {
                Icon(Icons.Default.Place, null)
                Text("Usar mi ubicación actual")
            }

            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth().height(100.dp))

            Button(
                onClick = {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {
                        val base64Image = profileBitmap?.let { bitmap ->
                            val outputStream = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
                            Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
                        } ?: ""

                        viewModel.updateProfile(
                            UserProfile(
                                id = userId,
                                nombre = fullName,
                                email = email,
                                phone = phone,
                                age = age,
                                zone = zone,
                                posicion = "Completar",
                                description = description,
                                photoBase64 = base64Image
                            )
                        )
                        onSaveClick()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(54.dp)
            ) {
                Text("Guardar Cambios")
            }
        }
    }
}