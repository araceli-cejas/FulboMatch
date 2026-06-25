package com.matchball.fulbomatch.data.model

data class UserProfile(
    val id: String = "",
    val nombre: String = "",
    val email: String = "",
    val posicion: String = "",
    val nivel: String = "",
    val phone: String = "",
    val age: String = "",
    val zone: String = "",
    val description: String = "",
    val photoBase64: String = "",
    val totalPartidos: Int = 0,
    val partidosCreados: Int = 0,
    val partidosAnotados: Int = 0
)