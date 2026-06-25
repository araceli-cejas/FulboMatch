package com.matchball.fulbomatch

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.firebase.Timestamp
import com.matchball.fulbomatch.data.model.Notification
import com.matchball.fulbomatch.ui.screens.NotificationCard
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertTrue

class NotificationCardTest {

    // Esta regla nos permite montar componentes de Compose aislados en el entorno de prueba
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun notificationCard_showsCorrectData() {
        // 1. PREPARACIÓN (Arrange): Creamos datos falsos para la prueba
        val mockNotification = Notification(
            id = "123",
            title = "Prueba de UI",
            message = "Este es un mensaje de prueba para la facultad",
            type = "info",
            isRead = false,
            timestamp = Timestamp.now()
        )

        // 2. EJECUCIÓN (Act): Montamos solo la tarjeta (no toda la pantalla)
        composeTestRule.setContent {
            NotificationCard(
                notification = mockNotification,
                onClick = {},
                colors = TODO(),
                isDarkMode = TODO()
            )
        }

        // 3. VERIFICACIÓN (Assert): Comprobamos que los textos existan en la pantalla
        composeTestRule.onNodeWithText("Prueba de UI").assertIsDisplayed()
        composeTestRule.onNodeWithText("Este es un mensaje de prueba para la facultad").assertIsDisplayed()
    }

    @Test
    fun notificationCard_clickTriggersAction() {
        // Comprobamos que el modificador "clickable" funcione correctamente
        var clickRegistrado = false
        val mockNotification = Notification(title = "Click Test", message = "Toca aquí")

        composeTestRule.setContent {
            NotificationCard(
                notification = mockNotification,
                onClick = { clickRegistrado = true },
                colors = TODO(),
                isDarkMode = TODO()
            )
        }

        // Simulamos un toque en la tarjeta
        composeTestRule.onNodeWithText("Click Test").performClick()

        // Verificamos que la variable haya cambiado a true
        assertTrue(clickRegistrado)
    }
}