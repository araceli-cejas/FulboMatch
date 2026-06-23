package com.matchball.fulbomatch.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.matchball.fulbomatch.R
import com.matchball.fulbomatch.ui.auth.AuthUiState
import com.matchball.fulbomatch.ui.auth.AuthViewModel
import com.matchball.fulbomatch.ui.theme.GreenPrimary
import com.matchball.fulbomatch.ui.theme.GrayMedium
import com.matchball.fulbomatch.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecuperarContraseñaScreen(
    onBackClick: () -> Unit,
    viewModel: AuthViewModel = viewModel() // <- Inyectamos el ViewModel
) {
    var email by remember { mutableStateOf("") }

    // Observamos el estado para saber si se envió el mail
    val uiState by viewModel.uiState.collectAsState()

    // Mostramos la pantalla de éxito si Firebase confirma el envío
    val emailSent = uiState is AuthUiState.Success

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState())
            .semantics { contentDescription = "Pantalla de recuperación de contraseña" }
    ) {
        // TopAppBar
        TopAppBar(
            title = {
                Text(
                    text = "Recuperar contraseña",
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        viewModel.resetState() // Limpiar estado al salir
                        onBackClick()
                    },
                    modifier = Modifier.semantics { contentDescription = "Volver atrás" }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = White)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .semantics { contentDescription = "Formulario de recuperación" },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!emailSent) {
                // ... (Icono, Título y Descripción quedan igual)
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            color = GreenPrimary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icono),
                        contentDescription = "Icono candado",
                        modifier = Modifier.size(60.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "¿Olvidaste tu contraseña?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "No te preocupes. Ingresá el correo electrónico asociado a tu cuenta y te enviaremos un enlace para restablecerla.",
                    fontSize = 14.sp,
                    color = GrayMedium,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Correo electrónico",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("tu@email.com") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = "Ícono de email")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { contentDescription = "Campo de email para recuperación" },
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = GrayMedium.copy(alpha = 0.5f),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar error de Firebase si falla el envío
                if (uiState is AuthUiState.Error) {
                    Text(
                        text = (uiState as AuthUiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Botón enviar conectado a Firebase
                Button(
                    onClick = {
                        if (email.isNotEmpty()) {
                            viewModel.resetPassword(email)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .semantics { contentDescription = "Botón para enviar enlace de recuperación" },
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    enabled = email.isNotEmpty() && uiState !is AuthUiState.Loading
                ) {
                    if (uiState is AuthUiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = White
                        )
                    } else {
                        Text("Enviar enlace", fontSize = 16.sp, color = White, fontWeight = FontWeight.SemiBold)
                    }
                }
            } else {
                // ... (Pantalla de confirmación queda igual)
                Spacer(modifier = Modifier.height(60.dp))

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            color = GreenPrimary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✓",
                        fontSize = 48.sp,
                        color = GreenPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "¡Enlace enviado!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Te hemos enviado un enlace de recuperación al correo:\n$email",
                    fontSize = 18.sp,
                    color = GrayMedium,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        viewModel.resetState() // Limpiar el estado
                        onBackClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .semantics { contentDescription = "Botón para volver a login" },
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Text("Volver a iniciar sesión", fontSize = 16.sp, color = White, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}