package com.matchball.fulbomatch.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matchball.fulbomatch.data.model.UserProfile
import com.matchball.fulbomatch.data.repository.AuthRepository
import com.matchball.fulbomatch.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UserUiState {
    object Idle : UserUiState()
    object Loading : UserUiState()
    object Success : UserUiState()
    data class Error(val message: String) : UserUiState()
}

class UserViewModel : ViewModel() {
    private val userRepository = UserRepository()
    private val authRepository = AuthRepository()

    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Idle)
    val uiState: StateFlow<UserUiState> = _uiState

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile

    init {
        loadCurrentUserProfile()
    }

    fun loadCurrentUserProfile() {
        viewModelScope.launch {
            _uiState.value = UserUiState.Loading
            val userId = authRepository.currentUser?.uid
            if (userId != null) {
                val result = userRepository.getUserProfile(userId)
                if (result.isSuccess) {
                    _userProfile.value = result.getOrNull()
                    // ACÁ ESTÁ LA MAGIA: Cambiamos Success por Idle
                    _uiState.value = UserUiState.Idle
                } else {
                    _uiState.value = UserUiState.Error("Error al cargar perfil")
                }
            } else {
                _uiState.value = UserUiState.Error("Usuario no autenticado")
            }
        }
    }

    fun logout() {
        authRepository.logout()
    }

    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            _uiState.value = UserUiState.Loading
            val result = userRepository.updateUserProfile(profile)
            if (result.isSuccess) {
                _userProfile.value = profile // Actualizamos el estado local
                _uiState.value = UserUiState.Success
            } else {
                _uiState.value = UserUiState.Error("Error al guardar el perfil")
            }
        }
    }

    fun resetState() {
        _uiState.value = UserUiState.Idle
    }
}