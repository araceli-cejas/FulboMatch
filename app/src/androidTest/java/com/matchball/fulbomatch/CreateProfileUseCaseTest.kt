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

class CreateProfileUseCaseTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun createProfileUseCase_userFillsFormAndSaves() {
        var saveProfileClicked = false

        composeTestRule.setContent {
            EditProfileMock(
                onSaveClick = { saveProfileClicked = true }
            )
        }

        // Llenamos el formulario simulando tipeo rápido
        composeTestRule.onNodeWithText("Nombre completo").performTextInput("Martín Palermo")
        composeTestRule.onNodeWithText("Teléfono").performTextInput("1123456789")
        composeTestRule.onNodeWithText("Edad").performTextInput("25")
        composeTestRule.onNodeWithText("Zona").performTextInput("Avellaneda")

        // Clic en Guardar
        composeTestRule.onNodeWithText("Guardar").performClick()

        assertTrue(saveProfileClicked)
    }
}

@Composable
fun EditProfileMock(onSaveClick: () -> Unit) {
    Column {
        TextField(value = "", onValueChange = {}, label = { Text("Nombre completo") })
        TextField(value = "", onValueChange = {}, label = { Text("Teléfono") })
        TextField(value = "", onValueChange = {}, label = { Text("Edad") })
        TextField(value = "", onValueChange = {}, label = { Text("Zona") })
        Button(onClick = onSaveClick) { Text("Guardar") }
    }
}