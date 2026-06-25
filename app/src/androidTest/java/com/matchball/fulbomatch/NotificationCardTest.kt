package com.matchball.fulbomatch

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.firebase.Timestamp
import com.matchball.fulbomatch.data.model.Notification
import com.matchball.fulbomatch.ui.screens.NotificationCard
import com.matchball.fulbomatch.ui.screens.NotificationColors
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class NotificationCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ✅ FIX: construimos NotificationColors con valores reales
    // No podemos llamar notificationColors() porque es private,
    // pero podemos construir el data class directamente
    private val testColors = NotificationColors(
        background = Color.White,
        cardBackground = Color.White,
        textPrimary = Color.Black,
        textSecondary = Color.DarkGray,
        textMuted = Color.Gray,
        headerIcon = Color.Black,
        icon = Color.Gray,
        accent = Color.Green,
        divider = Color.LightGray,
        border = Color.LightGray,
        bottomBarBackground = Color.White,
        bottomIcon = Color.Gray,
        unreadBadge = Color.Green
    )

    @Test
    fun notificationCard_showsCorrectData() {
        val mockNotification = Notification(
            id = "123",
            title = "Prueba de UI",
            message = "Este es un mensaje de prueba para la facultad",
            type = "info",
            isRead = false,
            timestamp = Timestamp.now()
        )

        composeTestRule.setContent {
            NotificationCard(
                notification = mockNotification,
                onClick = {},
                colors = testColors,   // ✅ FIX: reemplaza TODO()
                isDarkMode = false      // ✅ FIX: reemplaza TODO()
            )
        }

        composeTestRule.onNodeWithText("Prueba de UI").assertIsDisplayed()
        composeTestRule.onNodeWithText("Este es un mensaje de prueba para la facultad").assertIsDisplayed()
    }

    @Test
    fun notificationCard_clickTriggersAction() {
        var clickRegistrado = false

        val mockNotification = Notification(
            title = "Click Test",
            message = "Toca aquí"
        )

        composeTestRule.setContent {
            NotificationCard(
                notification = mockNotification,
                onClick = { clickRegistrado = true },
                colors = testColors,   // ✅ FIX: reemplaza TODO()
                isDarkMode = false      // ✅ FIX: reemplaza TODO()
            )
        }

        composeTestRule.onNodeWithText("Click Test").performClick()

        assertTrue(clickRegistrado)
    }
}