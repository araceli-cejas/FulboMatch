package com.matchball.fulbomatch.data.repository

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.matchball.fulbomatch.data.local.AppDatabase
import com.matchball.fulbomatch.data.local.toEntity
import com.matchball.fulbomatch.data.model.Partido
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class PartidoRepository(context: Context) {

    // Firebase
    private val db = FirebaseFirestore.getInstance()
    private val partidosCollection = db.collection("partidos")

    // Room (Local)
    private val database = AppDatabase.getDatabase(context)
    private val partidoDao = database.partidoDao()

    // FLUJO REAL: La UI escucha de Room. Si Room cambia, la UI se entera sola.
    val partidosLocalFlow: Flow<List<Partido>> = partidoDao.getAllPartidos().map { entities ->
        entities.map { it.toPartido() }
    }

    // Sincronizar Firebase con Room (Descarga y guarda localmente)
    suspend fun refreshPartidos(): Result<Unit> {
        return try {
            val snapshot = partidosCollection.get().await()
            val partidosCloud = snapshot.toObjects(Partido::class.java)

            // Pasamos a entidades de Room
            val entidadesLocal = partidosCloud.map { it.toEntity() }

            // Actualizamos la caché local
            partidoDao.clearAll()
            partidoDao.insertAll(entidadesLocal)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Crear partido (Sube a Firebase y refresca local)
    suspend fun createPartido(partido: Partido): Result<Unit> {
        return try {
            partidosCollection.document(partido.id).set(partido).await()
            refreshPartidos() // Sincroniza la caché
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Actualizar partido (Sube a Firebase y refresca local)
    suspend fun updatePartido(partido: Partido): Result<Unit> {
        return try {
            partidosCollection.document(partido.id).set(partido).await()
            refreshPartidos()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Borrar partido
    suspend fun deletePartido(partidoId: String): Result<Unit> {
        return try {
            partidosCollection.document(partidoId).delete().await()
            refreshPartidos()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Sumarse a partido
    suspend fun joinPartido(partidoId: String, userId: String): Result<Unit> {
        return try {
            val doc = partidosCollection.document(partidoId).get().await()
            val partido = doc.toObject(Partido::class.java)
            if (partido != null && !partido.jugadoresConfirmados.contains(userId)) {
                val nuevaLista = partido.jugadoresConfirmados + userId
                partidosCollection.document(partidoId).update("jugadoresConfirmados", nuevaLista).await()
                refreshPartidos()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Bajarse de partido
    suspend fun leavePartido(partidoId: String, userId: String): Result<Unit> {
        return try {
            val doc = partidosCollection.document(partidoId).get().await()
            val partido = doc.toObject(Partido::class.java)
            if (partido != null && partido.jugadoresConfirmados.contains(userId)) {
                val nuevaLista = partido.jugadoresConfirmados - userId
                partidosCollection.document(partidoId).update("jugadoresConfirmados", nuevaLista).await()
                refreshPartidos()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}