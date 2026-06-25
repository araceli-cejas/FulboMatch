package com.matchball.fulbomatch.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.matchball.fulbomatch.data.model.Partido

@Entity(tableName = "partidos_table")
@TypeConverters(StringListConverter::class)
data class PartidoEntity(
    @PrimaryKey val id: String,
    val titulo: String,
    val creadorId: String,
    val fecha: String,
    val hora: String,
    val lugar: String,
    val maxJugadores: Int,
    val precio: String,
    val nivel: String,
    val superficie: String,
    val descripcion: String,
    val jugadoresConfirmados: List<String>,
    val status: String = "PENDIENTE",
    val golesLocal: Int = 0,
    val golesVisitante: Int = 0,
    val duracion: Int = 0,           // ← NUEVO
    val tarjetasAmarillas: Int = 0,  // ← NUEVO
    val tarjetasRojas: Int = 0       // ← NUEVO
) {
    fun toPartido(): Partido = Partido(
        id = id,
        titulo = titulo,
        creadorId = creadorId,
        fecha = fecha,
        hora = hora,
        lugar = lugar,
        maxJugadores = maxJugadores,
        precio = precio,
        nivel = nivel,
        superficie = superficie,
        descripcion = descripcion,
        jugadoresConfirmados = jugadoresConfirmados,
        status = status,
        golesLocal = golesLocal,
        golesVisitante = golesVisitante,
        duracion = duracion,           // ← NUEVO
        tarjetasAmarillas = tarjetasAmarillas, // ← NUEVO
        tarjetasRojas = tarjetasRojas  // ← NUEVO
    )
}

class StringListConverter {
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType) ?: emptyList()
    }
}

fun Partido.toEntity(): PartidoEntity = PartidoEntity(
    id = id,
    titulo = titulo,
    creadorId = creadorId,
    fecha = fecha,
    hora = hora,
    lugar = lugar,
    maxJugadores = maxJugadores,
    precio = precio,
    nivel = nivel,
    superficie = superficie,
    descripcion = descripcion,
    jugadoresConfirmados = jugadoresConfirmados,
    status = status,
    golesLocal = golesLocal,
    golesVisitante = golesVisitante,
    duracion = duracion,           // ← NUEVO
    tarjetasAmarillas = tarjetasAmarillas, // ← NUEVO
    tarjetasRojas = tarjetasRojas  // ← NUEVO
)