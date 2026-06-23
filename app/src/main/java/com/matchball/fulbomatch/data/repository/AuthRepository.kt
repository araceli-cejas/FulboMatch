package com.matchball.fulbomatch.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FirebaseFirestore
import com.matchball.fulbomatch.data.model.UserProfile

class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // Usuario actual (null si no está logueado)
    val currentUser: FirebaseUser? get() = auth.currentUser

    // Login con email y contraseña
    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Registro con email y contraseña
    suspend fun register(email: String, password: String, nombre: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user!!

            // Guardar el perfil en Firestore
            val userProfile = UserProfile(
                id = user.uid,
                nombre = nombre,
                email = email
            )
            db.collection("users").document(user.uid).set(userProfile).await()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Cerrar sesión
    fun logout() {
        auth.signOut()
    }

    // Recuperar contraseña
    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}