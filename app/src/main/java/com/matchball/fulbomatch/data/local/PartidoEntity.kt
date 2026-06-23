package com.matchball.fulbomatch.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.matchball.fulbomatch.data.model.Partido

// 1. Declaramos que esta clase es una tabla en la base de datos local
@Entity(tableName = "partidos_table")
@TypeConverters(StringListConverter::class) // Le decimos cómo guardar las listas
data class PartidoEntity(
    @PrimaryKey val id: String, // La clave principal (usamos el mismo ID de Firebase)
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
    val jugadoresConfirmados: List<String> // Acá guardamos los IDs
) {
    // Función amiga para convertir desde esta entidad local al modelo de la app
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
        jugadoresConfirmados = jugadoresConfirmados
    )
}

// Esto convierte una lista de Strings a un JSON gigante y viceversa
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

// 3. Función amiga (Extensión) para convertir el modelo de Firebase a Entidad Local
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
    jugadoresConfirmados = jugadoresConfirmados
)