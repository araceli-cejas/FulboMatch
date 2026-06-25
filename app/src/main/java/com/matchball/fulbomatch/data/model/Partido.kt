package com.matchball.fulbomatch.data.model

data class Partido(
    val id: String = "",
    val titulo: String = "",
    val creadorId: String = "",
    val fecha: String = "",
    val hora: String = "",
    val lugar: String = "",
    val maxJugadores: Int = 10,
    val precio: String = "1500",
    val nivel: String = "Medio",
    val superficie: String = "Sintético",
    val descripcion: String = "",
    val jugadoresConfirmados: List<String> = emptyList(),
    val status: String = "PENDIENTE",
    val golesLocal: Int = 0,
    val golesVisitante: Int = 0
)