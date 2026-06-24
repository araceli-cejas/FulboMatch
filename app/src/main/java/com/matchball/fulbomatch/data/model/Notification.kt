package com.matchball.fulbomatch.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Notification(
    var id: String = "",
    var userId: String = "",
    var title: String = "",
    var message: String = "",
    var type: String = "info",

// Le decimos explícitamente a Firebase cómo se llama el campo en la base de datos
    @get:PropertyName("isRead")
    @set:PropertyName("isRead")
    var isRead: Boolean = false,

    // Cambiamos Long por Timestamp nativo de Firebase
    var timestamp: Timestamp = Timestamp.now()
)