package com.matchball.fulbomatch

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
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

        // ✅ FIX: se quitó performScrollTo() — no hay contenedor scrolleable
        // y todos los elementos entran en pantalla sin scroll
        composeTestRule.onNodeWithText("Crear partido").performClick()

        assertTrue(matchCreated)
    }
}

// ✅ FIX: los TextField ahora tienen estado real con remember + mutableStateOf
// Antes tenían value = "" hardcodeado, lo que hacía que performTextInput
// escribiera pero el composable nunca reflejara el cambio
@Composable
fun CreateMatchMock(onCreateClick: () -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var lugar by remember { mutableStateOf("") }

    Column {
        TextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título del partido") }
        )
        TextField(
            value = lugar,
            onValueChange = { lugar = it },
            label = { Text("Lugar") }
        )
        Button(onClick = onCreateClick) {
            Text("Crear partido")
        }
    }
}