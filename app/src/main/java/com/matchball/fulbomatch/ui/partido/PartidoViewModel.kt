package com.matchball.fulbomatch.ui.partido

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.matchball.fulbomatch.data.model.Partido
import com.matchball.fulbomatch.data.model.UserProfile
import com.matchball.fulbomatch.data.repository.AuthRepository
import com.matchball.fulbomatch.data.repository.PartidoRepository
import com.matchball.fulbomatch.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

sealed class PartidoUiState {
    object Idle : PartidoUiState()
    object Loading : PartidoUiState()
    object Success : PartidoUiState()
    data class Error(val message: String) : PartidoUiState()
}

class PartidoViewModel(application: Application) : AndroidViewModel(application) {

    // Pasamos 'application' para que el repositorio inicialice Room sin problemas
    private val partidoRepository = PartidoRepository(application)
    private val authRepository = AuthRepository()
    private val userRepository = UserRepository()

    private val _uiState = MutableStateFlow<PartidoUiState>(PartidoUiState.Idle)
    val uiState: StateFlow<PartidoUiState> = _uiState.asStateFlow()

    private val _partidos = MutableStateFlow<List<Partido>>(emptyList())
    val partidos: StateFlow<List<Partido>> = _partidos.asStateFlow()

    private val _jugadoresConfirmados = MutableStateFlow<List<UserProfile>>(emptyList())
    val jugadoresConfirmados: StateFlow<List<UserProfile>> = _jugadoresConfirmados.asStateFlow()

    val currentUserId: String? get() = authRepository.currentUser?.uid

    init {
        // --- CONEXIÓN ROOM OBLIGATORIA ---
        // Nos quedamos escuchando la base de datos local de forma permanente.
        // Si Room cambia (porque agregamos, editamos o sincronizamos), la UI se entera sola.
        viewModelScope.launch {
            partidoRepository.partidosLocalFlow.collect { listaPartidos ->
                _partidos.value = listaPartidos
            }
        }
        // Hacemos una carga inicial automática al abrir la app
        loadPartidos()
    }

    // Cambiamos la lógica: Ahora carga significa "sincronizar Firebase con Room"
    fun loadPartidos() {
        viewModelScope.launch {
            _uiState.value = PartidoUiState.Loading
            val result = partidoRepository.refreshPartidos()
            if (result.isSuccess) {
                _uiState.value = PartidoUiState.Idle
            } else {
                _uiState.value = PartidoUiState.Error("Error al sincronizar datos con la nube")
            }
        }
    }

    fun resetState() {
        _uiState.value = PartidoUiState.Idle
    }

    fun crearPartido(titulo: String, fecha: String, hora: String, lugar: String, maxJugadores: Int) {
        viewModelScope.launch {
            _uiState.value = PartidoUiState.Loading
            val currentUser = authRepository.currentUser

            if (currentUser == null) {
                _uiState.value = PartidoUiState.Error("Tenés que iniciar sesión para organizar un partido")
                return@launch
            }

            // Generamos un ID único con UUID para evitar que Room pise partidos vacíos
            val idUnico = UUID.randomUUID().toString()

            val nuevoPartido = Partido(
                id = idUnico,
                titulo = titulo,
                creadorId = currentUser.uid,
                fecha = fecha,
                hora = hora,
                lugar = lugar,
                maxJugadores = maxJugadores,
                jugadoresConfirmados = listOf(currentUser.uid)
            )

            val result = partidoRepository.createPartido(nuevoPartido)

            if (result.isSuccess) {
                _uiState.value = PartidoUiState.Success
            } else {
                _uiState.value = PartidoUiState.Error(result.exceptionOrNull()?.message ?: "Error al crear el partido")
            }
        }
    }

    fun actualizarPartido(partidoActualizado: Partido) {
        viewModelScope.launch {
            _uiState.value = PartidoUiState.Loading
            val result = partidoRepository.updatePartido(partidoActualizado)
            if (result.isSuccess) {
                _uiState.value = PartidoUiState.Success
            } else {
                _uiState.value = PartidoUiState.Error("Error al actualizar el partido")
            }
        }
    }

    fun borrarPartido(partidoId: String) {
        viewModelScope.launch {
            _uiState.value = PartidoUiState.Loading
            val result = partidoRepository.deletePartido(partidoId)
            if (result.isSuccess) {
                _uiState.value = PartidoUiState.Success
            } else {
                _uiState.value = PartidoUiState.Error("Error al cancelar el partido")
            }
        }
    }

    fun sumarseAPartido(partidoId: String) {
        viewModelScope.launch {
            _uiState.value = PartidoUiState.Loading
            currentUserId?.let { uid ->
                val result = partidoRepository.joinPartido(partidoId, uid)
                if (result.isSuccess) {
                    _uiState.value = PartidoUiState.Success
                } else {
                    _uiState.value = PartidoUiState.Error("Error al sumarse al partido")
                }
            }
        }
    }

    fun bajarseDePartido(partidoId: String) {
        viewModelScope.launch {
            _uiState.value = PartidoUiState.Loading
            currentUserId?.let { uid ->
                val result = partidoRepository.leavePartido(partidoId, uid)
                if (result.isSuccess) {
                    _uiState.value = PartidoUiState.Success
                } else {
                    _uiState.value = PartidoUiState.Error("Error al bajarse del partido")
                }
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