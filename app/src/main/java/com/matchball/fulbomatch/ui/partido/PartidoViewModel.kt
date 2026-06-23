package com.matchball.fulbomatch.ui.partido

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matchball.fulbomatch.data.model.Partido
import com.matchball.fulbomatch.data.model.UserProfile
import com.matchball.fulbomatch.data.repository.AuthRepository
import com.matchball.fulbomatch.data.repository.PartidoRepository
import com.matchball.fulbomatch.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Estados para manejar la UI de carga y errores
sealed class PartidoUiState {
    object Idle : PartidoUiState()
    object Loading : PartidoUiState()
    object Success : PartidoUiState()
    data class Error(val message: String) : PartidoUiState()
}

class PartidoViewModel : ViewModel() {
    private val partidoRepository = PartidoRepository()
    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()

    private val _uiState = MutableStateFlow<PartidoUiState>(PartidoUiState.Idle)
    val uiState: StateFlow<PartidoUiState> = _uiState

    // Lista de partidos disponibles
    private val _partidos = MutableStateFlow<List<Partido>>(emptyList())
    val partidos: StateFlow<List<Partido>> = _partidos

    init {
        // Al instanciar el ViewModel, cargamos los partidos automáticamente
        loadPartidos()
    }

    private val _jugadoresConfirmados = MutableStateFlow<List<UserProfile>>(emptyList())
    val jugadoresConfirmados: StateFlow<List<UserProfile>> = _jugadoresConfirmados

    fun loadPartidos() {
        viewModelScope.launch {
            _uiState.value = PartidoUiState.Loading
            val result = partidoRepository.getPartidos()
            if (result.isSuccess) {
                _partidos.value = result.getOrDefault(emptyList())
                _uiState.value = PartidoUiState.Idle
            } else {
                _uiState.value = PartidoUiState.Error(result.exceptionOrNull()?.message ?: "Error al cargar los partidos")
            }
        }
    }

    fun crearPartido(titulo: String, fecha: String, hora: String, lugar: String, maxJugadores: Int) {
        viewModelScope.launch {
            _uiState.value = PartidoUiState.Loading
            val currentUser = authRepository.currentUser

            if (currentUser == null) {
                _uiState.value = PartidoUiState.Error("Tenés que iniciar sesión para organizar un partido")
                return@launch
            }

            // Acá es donde definimos 'nuevoPartido'
            val nuevoPartido = Partido(
                titulo = titulo,
                creadorId = currentUser.uid,
                fecha = fecha,
                hora = hora,
                lugar = lugar,
                maxJugadores = maxJugadores,
                jugadoresConfirmados = listOf(currentUser.uid) // El creador se suma automáticamente
            )

            // Y acá lo guardamos en Firebase
            val result = partidoRepository.createPartido(nuevoPartido)

            if (result.isSuccess) {
                _uiState.value = PartidoUiState.Success
            } else {
                _uiState.value = PartidoUiState.Error(result.exceptionOrNull()?.message ?: "Error al crear el partido")
            }
        }
    }

    fun resetState() {
        _uiState.value = PartidoUiState.Idle
    }

    // Obtenemos el ID del usuario actual
    val currentUserId: String? get() = authRepository.currentUser?.uid

    // Función para sumarse (Agrega el ID al array en Firestore)
    fun sumarseAPartido(partidoId: String) {
        viewModelScope.launch {
            _uiState.value = PartidoUiState.Loading
            currentUserId?.let { uid ->
                val result = partidoRepository.joinPartido(partidoId, uid)
                if (result.isSuccess) {
                    _uiState.value = PartidoUiState.Success
                    loadPartidos() // Recargamos para actualizar los cupos
                } else {
                    _uiState.value = PartidoUiState.Error("Error al sumarse")
                }
            }
        }
    }

    // Función para bajarse (Quita el ID del array en Firestore)
    fun bajarseDePartido(partidoId: String) {
        viewModelScope.launch {
            _uiState.value = PartidoUiState.Loading
            currentUserId?.let { uid ->
                val result = partidoRepository.leavePartido(partidoId, uid)
                if (result.isSuccess) {
                    _uiState.value = PartidoUiState.Success
                    loadPartidos() // Recargamos para liberar el cupo
                } else {
                    _uiState.value = PartidoUiState.Error("Error al bajarse")
                }
            }
        }
    }

    // Guardar los cambios editados
    fun actualizarPartido(partidoActualizado: Partido) {
        viewModelScope.launch {
            _uiState.value = PartidoUiState.Loading
            val result = partidoRepository.updatePartido(partidoActualizado)
            if (result.isSuccess) {
                // Solo dejamos el Success para que la UI navegue para atrás
                _uiState.value = PartidoUiState.Success
            } else {
                _uiState.value = PartidoUiState.Error("Error al actualizar el partido")
            }
        }
    }

    // Cancelar/Borrar partido
    fun borrarPartido(partidoId: String) {
        viewModelScope.launch {
            _uiState.value = PartidoUiState.Loading
            val result = partidoRepository.deletePartido(partidoId)
            if (result.isSuccess) {
                // Solo dejamos el Success para que la UI navegue para atrás
                _uiState.value = PartidoUiState.Success
            } else {
                _uiState.value = PartidoUiState.Error("Error al cancelar el partido")
            }
        }
    }

    fun cargarJugadoresConfirmados(userIds: List<String>) {
        viewModelScope.launch {
            if (userIds.isEmpty()) {
                _jugadoresConfirmados.value = emptyList()
                return@launch
            }

            val result = userRepository.getUsersProfiles(userIds)
            if (result.isSuccess) {
                _jugadoresConfirmados.value = result.getOrDefault(emptyList())
            }
        }
    }
}