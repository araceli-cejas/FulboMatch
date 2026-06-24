package com.matchball.fulbomatch

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class JoinMatchUseCaseTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun joinMatchUseCase_userClicksJoinButton() {
        var joinedMatch = false

        composeTestRule.setContent {
            MatchDetailMock(
                matchTitle = "Fútbol 5 - Jueves",
                onJoinClick = { joinedMatch = true }
            )
        }

        // Verificamos que el título del partido se renderice en pantalla
        composeTestRule.onNodeWithText("Fútbol 5 - Jueves").assertIsDisplayed()

        // Buscamos el botón de unirse y lo presionamos
        composeTestRule.onNodeWithText("Unirme al partido").performClick()

        assertTrue(joinedMatch)
    }
}

@Composable
fun MatchDetailMock(matchTitle: String, onJoinClick: () -> Unit) {
    Column {
        Text(matchTitle)
        Button(onClick = onJoinClick) { Text("Unirme al partido") }
    }
}