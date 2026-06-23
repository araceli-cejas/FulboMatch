package com.matchball.fulbomatch.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.matchball.fulbomatch.data.model.Partido
import kotlinx.coroutines.tasks.await

class PartidoRepository {
    private val db = FirebaseFirestore.getInstance()
    private val partidosCollection = db.collection("partidos")

    // Crear un nuevo partido
    suspend fun createPartido(partido: Partido): Result<Unit> {
        return try {
            val document = partidosCollection.document() // Genera un ID único automáticamente
            val partidoConId = partido.copy(id = document.id)
            document.set(partidoConId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener todos los partidos disponibles
    suspend fun getPartidos(): Result<List<Partido>> {
        return try {
            val snapshot = partidosCollection.get().await()
            val partidos = snapshot.toObjects(Partido::class.java)
            Result.success(partidos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Sumarse a un partido (Agrega el ID del usuario al array de confirmados)
    suspend fun joinPartido(partidoId: String, userId: String): Result<Unit> {
        return try {
            partidosCollection.document(partidoId)
                .update("jugadoresConfirmados", FieldValue.arrayUnion(userId))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Bajarse de un partido (Quita el ID del usuario del array)
    suspend fun leavePartido(partidoId: String, userId: String): Result<Unit> {
        return try {
            partidosCollection.document(partidoId)
                .update("jugadoresConfirmados", FieldValue.arrayRemove(userId))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Borrar partido (Solo el organizador debería poder hacer esto)
    suspend fun deletePartido(partidoId: String): Result<Unit> {
        return try {
            partidosCollection.document(partidoId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}