package com.matchball.fulbomatch.ui.notification

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.matchball.fulbomatch.data.model.Notification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()

    fun listenToNotifications(userId: String) {
        if (userId.isBlank()) return

        db.collection("notifications")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("FIREBASE_NOTIS", "Error en el listener de Firebase: ${error.message}")
                    return@addSnapshotListener
                }

                try {
                    val notifs = snapshot?.documents?.mapNotNull { doc ->
                        try {
                            // Mapeo seguro de cada documento de Firestore al modelo de datos
                            doc.toObject(Notification::class.java)?.copy(id = doc.id)
                        } catch (e: Exception) {
                            Log.e("FIREBASE_NOTIS", "Error al deserializar el documento ${doc.id}: ${e.message}")
                            null
                        }
                    } ?: emptyList()

                    _notifications.value = notifs
                } catch (e: Exception) {
                    Log.e("FIREBASE_NOTIS", "Error general procesando la lista de notificaciones: ${e.message}")
                }
            }
    }

    fun markAsRead(notificationId: String) {
        Log.d("FIREBASE_NOTIS", "Click detectado. Intentando marcar ID: '$notificationId'")

        // Evitamos que un ID vacío rompa la referencia de documento de Firestore
        if (notificationId.isBlank()) {
            Log.e("FIREBASE_NOTIS", "Cancelando operación: El ID recibido está vacío o en blanco.")
            return
        }

        try {
            db.collection("notifications").document(notificationId)
                .update("isRead", true)
                .addOnSuccessListener {
                    Log.d("FIREBASE_NOTIS", "Documento $notificationId actualizado correctamente en Firestore.")
                }
                .addOnFailureListener { e ->
                    Log.e("FIREBASE_NOTIS", "Firestore rechazó la actualización de escritura: ${e.message}")
                }
        } catch (e: Exception) {
            Log.e("FIREBASE_NOTIS", "Excepción capturada al intentar actualizar el documento: ${e.message}")
            e.printStackTrace()
        }
    }

    fun markAllAsRead(userId: String) {
        _notifications.value.filter { !it.isRead }.forEach { notif ->
            markAsRead(notif.id)
        }
    }
}