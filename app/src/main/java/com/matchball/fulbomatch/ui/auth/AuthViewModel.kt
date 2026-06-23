package com.matchball.fulbomatch.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matchball.fulbomatch.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Estados posibles de la pantalla
sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    // Verificar si ya hay sesión activa
    val isLoggedIn: Boolean get() = repository.currentUser != null

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = repository.login(email, password)
            _uiState.value = if (result.isSuccess) {
                AuthUiState.Success
            } else {
                AuthUiState.Error(result.exceptionOrNull()?.message ?: "Error al iniciar sesión")
            }
        }
    }

    fun register(email: String, password: String, nombre: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = repository.register(email, password, nombre) // <-- Pasamos el nombre
            _uiState.value = if (result.isSuccess) {
                AuthUiState.Success
            } else {
                AuthUiState.Error(result.exceptionOrNull()?.message ?: "Error al registrarse")
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = repository.resetPassword(email)
            _uiState.value = if (result.isSuccess) {
                AuthUiState.Success
            } else {
                AuthUiState.Error(result.exceptionOrNull()?.message ?: "Error al enviar email")
            }
        }
    }

    fun logout() {
        repository.logout()
        _uiState.value = AuthUiState.Idle
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}