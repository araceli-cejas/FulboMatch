package com.matchball.fulbomatch.data.repository

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.matchball.fulbomatch.data.model.UserProfile
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    // Buscar un usuario específico por su ID
    suspend fun getUserProfile(userId: String): Result<UserProfile?> {
        return try {
            val snapshot = usersCollection.document(userId).get().await()
            val profile = snapshot.toObject(UserProfile::class.java)
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Buscar varios usuarios a la vez (ideal para la lista de confirmados)
    suspend fun getUsersProfiles(userIds: List<String>): Result<List<UserProfile>> {
        if (userIds.isEmpty()) return Result.success(emptyList())

        return try {
            // Firestore permite buscar hasta 10 IDs de una sola vez con 'in'
            val chunks = userIds.chunked(10)
            val resultList = mutableListOf<UserProfile>()

            for (chunk in chunks) {
                val snapshot = usersCollection
                    .whereIn(FieldPath.documentId(), chunk)
                    .get()
                    .await()
                resultList.addAll(snapshot.toObjects(UserProfile::class.java))
            }

            Result.success(resultList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Actualizar o crear el perfil del usuario
    suspend fun updateUserProfile(profile: UserProfile): Result<Unit> {
        return try {
            usersCollection.document(profile.id).set(profile).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}