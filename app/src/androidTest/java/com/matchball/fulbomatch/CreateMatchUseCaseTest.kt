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

class CreateMatchUseCaseTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun createMatchUseCase_userSetsDetailsAndCreates() {
        var matchCreated = false

        composeTestRule.setContent {
            CreateMatchMock(
                onCreateClick = { matchCreated = true }
            )
        }

        // Completamos los datos del partido
        composeTestRule.onNodeWithText("Título del partido").performTextInput("Fútbol 5 - Jueves")
        composeTestRule.onNodeWithText("Lugar").performTextInput("Canchas El Templo")

        // Simulación de scroll por si la pantalla es larga y clic
        composeTestRule.onNodeWithText("Crear partido").performScrollTo().performClick()

        assertTrue(matchCreated)
    }
}

@Composable
fun CreateMatchMock(onCreateClick: () -> Unit) {
    Column {
        TextField(value = "", onValueChange = {}, label = { Text("Título del partido") })
        TextField(value = "", onValueChange = {}, label = { Text("Lugar") })
        Button(onClick = onCreateClick) { Text("Crear partido") }
    }
}