package com.matchball.fulbomatch.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: AuthViewModel = viewModel() // <- Agregamos el ViewModel
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var acceptedTerms by remember { mutableStateOf(false) }

    // Estado de error local para validaciones
    var localError by remember { mutableStateOf<String?>(null) }

    // Observar el estado de Firebase
    val uiState by viewModel.uiState.collectAsState()

    // Manejar el éxito del registro
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            viewModel.resetState()
            onRegisterSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .semantics { contentDescription = "Pantalla de registro" },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registro",
            fontSize = 12.sp,
            color = GrayMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp)
                    .semantics { contentDescription = "Formulario de registro" },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ... (Mantengo tu UI de Logo, Textos y Nombre completo igual)
                Image(
                    painter = painterResource(id = R.drawable.logo_completo),
                    contentDescription = "Logo FulboMatch",
                    modifier = Modifier
                        .width(280.dp)
                        .height(90.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Creá tu cuenta",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF005C1F),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sumate a la comunidad y empezá a jugar.",
                    fontSize = 16.sp,
                    color = GrayMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "Nombre completo",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Ej: Lionel Messi") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = "Ícono de persona")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { contentDescription = "Campo de nombre" },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = GrayMedium.copy(alpha = 0.5f),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedPlaceholderColor = GrayMedium,
                        unfocusedPlaceholderColor = GrayMedium,
                        cursorColor = GreenPrimary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Email",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; localError = null }, // Limpiar error al tipear
                    placeholder = { Text("tu@email.com") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = "Ícono de email")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { contentDescription = "Campo de email" },
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = GrayMedium.copy(alpha = 0.5f),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedPlaceholderColor = GrayMedium,
                        unfocusedPlaceholderColor = GrayMedium,
                        cursorColor = GreenPrimary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Contraseña",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; localError = null }, // Limpiar error al tipear
                    placeholder = { Text("Mínimo 8 caracteres") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { contentDescription = "Campo de contraseña" },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = GrayMedium.copy(alpha = 0.5f),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedPlaceholderColor = GrayMedium,
                        unfocusedPlaceholderColor = GrayMedium,
                        cursorColor = GreenPrimary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Confirmar Contraseña",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; localError = null }, // Limpiar error al tipear
                    placeholder = { Text("Repetí tu contraseña") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { contentDescription = "Campo de confirmación de contraseña" },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    visualTransformation = if (confirmPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = GrayMedium.copy(alpha = 0.5f),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedPlaceholderColor = GrayMedium,
                        unfocusedPlaceholderColor = GrayMedium,
                        cursorColor = GreenPrimary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Términos y condiciones
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { contentDescription = "Aceptar términos y condiciones" },
                    verticalAlignment = Alignment.Top
                ) {
                    Checkbox(
                        checked = acceptedTerms,
                        onCheckedChange = { acceptedTerms = it },
                        colors = CheckboxDefaults.colors(checkedColor = GreenPrimary),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "Acepto los Términos y Condiciones y la Política de Privacidad.",
                        fontSize = 12.sp,
                        color = GrayMedium,
                        modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Mostrar errores (locales o de Firebase)
                if (localError != null) {
                    Text(
                        text = localError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                } else if (uiState is AuthUiState.Error) {
                    Text(
                        text = (uiState as AuthUiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón crear cuenta conectado a Firebase
                Button(
                    onClick = {
                        when {
                            name.isBlank() -> localError = "Ingresá tu nombre"
                            email.isBlank() -> localError = "Ingresá un email válido"
                            password.length < 6 -> localError = "La contraseña debe tener al menos 6 caracteres" // Regla de Firebase
                            password != confirmPassword -> localError = "Las contraseñas no coinciden"
                            else -> {
                                localError = null
                                // Llamamos al ViewModel para crear en Firebase
                                viewModel.register(email, password, name)
                                // Ojo: Aca todavia no guardamos el 'name', eso lo haremos en Firestore más adelante
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .semantics { contentDescription = "Botón para crear cuenta" },
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    enabled = acceptedTerms && uiState !is AuthUiState.Loading // Deshabilitar si está cargando
                ) {
                    if (uiState is AuthUiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = White
                        )
                    } else {
                        Text(
                            text = "Crear cuenta →",
                            fontSize = 16.sp,
                            color = White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Ya tenés cuenta
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "¿Ya tenés cuenta? ",
                        color = GrayMedium,
                        fontSize = 14.sp
                    )

                    Text(
                        text = "Iniciar sesión",
                        color = GreenPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable { onBackClick() }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}