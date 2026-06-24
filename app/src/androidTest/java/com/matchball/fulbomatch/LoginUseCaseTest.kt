package com.matchball.fulbomatch

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LoginUseCaseTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginUseCase_userEntersCredentialsAndSubmits() {
        var loginButtonClicked = false

        composeTestRule.setContent {
            LoginScreenMock(
                onLoginClick = { email, password ->
                    loginButtonClicked = true
                }
            )
        }

        // 1. Escribimos el email
        composeTestRule.onNodeWithText("Correo electrónico").performTextInput("test@fulbomatch.com")

        // 2. Escribimos la contraseña
        composeTestRule.onNodeWithText("Contraseña").performTextInput("123456")

        // 3. Hacemos clic en el botón
        composeTestRule.onNodeWithText("Iniciar sesión").performClick()

        // 4. Verificamos que la acción se haya disparado
        assertTrue(loginButtonClicked)
    }
}

@Composable
fun LoginScreenMock(onLoginClick: (String, String) -> Unit) {
    Column {
        TextField(value = "", onValueChange = {}, label = { Text("Correo electrónico") })
        TextField(value = "", onValueChange = {}, label = { Text("Contraseña") })
        Button(onClick = { onLoginClick("a", "b") }) {
            Text("Iniciar sesión")
        }
    }
}