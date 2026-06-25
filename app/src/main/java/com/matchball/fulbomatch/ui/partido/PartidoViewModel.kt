package com.matchball.fulbomatch.ui.partido

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.matchball.fulbomatch.data.model.Partido
import com.matchball.fulbomatch.data.model.UserProfile
import com.matchball.fulbomatch.data.repository.AuthRepository
import com.matchball.fulbomatch.data.repository.PartidoRepository
import com.matchball.fulbomatch.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

sealed class PartidoUiState {
    object Idle : PartidoUiState()
    object Loading : PartidoUiState()
    object Success : PartidoUiState()
    data class Error(val message: String) : PartidoUiState()
}

class PartidoViewModel(application: Application) : AndroidViewModel(application) {

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

    val misPartidos = partidos.value.filter { it.creadorId == currentUserId }
    val creadosCount = misPartidos.size
    val sumadoCount = partidos.value.filter { it.jugadoresConfirmados.contains(currentUserId) }.size

    init {
        viewModelScope.launch {
            partidoRepository.partidosLocalFlow.collect { listaPartidos ->
                _partidos.value = listaPartidos
            }
        }
        loadPartidos()
    }

    val partidosUsuario = _partidos.map { lista ->
        val uid = currentUserId ?: ""
        lista.filter { partido ->
            partido.creadorId == uid || partido.jugadoresConfirmados.contains(uid)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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
                // Notificar a los jugadores que el partido cambió
                partidoActualizado.jugadoresConfirmados.forEach { playerUid ->
                    if (playerUid != partidoActualizado.creadorId) {
                        sendNotification(
                            userId = playerUid,
                            title = "Partido actualizado",
                            message = "El organizador actualizó los detalles de: ${partidoActualizado.titulo}.",
                            type = "match_update"
                        )
                    }
                }
                _uiState.value = PartidoUiState.Success
            } else {
                _uiState.value = PartidoUiState.Error("Error al actualizar el partido")
            }
        }
    }

    fun borrarPartido(partidoId: String) {
        viewModelScope.launch {
            _uiState.value = PartidoUiState.Loading
            val partido = _partidos.value.find { it.id == partidoId }
            val result = partidoRepository.deletePartido(partidoId)
            if (result.isSuccess) {
                // Notificar a los jugadores que el partido se canceló
                partido?.jugadoresConfirmados?.forEach { playerUid ->
                    if (playerUid != partido.creadorId) {
                        sendNotification(
                            userId = playerUid,
                            title = "Partido cancelado",
                            message = "Se ha cancelado el partido: ${partido.titulo}.",
                            type = "match_cancelled"
                        )
                    }
                }
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
                // Buscamos el partido ANTES de la operación para asegurar que lo tenemos
                val partido = _partidos.value.find { it.id == partidoId }
                
                val result = partidoRepository.joinPartido(partidoId, uid)
                if (result.isSuccess) {
                    val userResult = userRepository.getUsersProfiles(listOf(uid))
                    val playerName = userResult.getOrNull()?.firstOrNull()?.nombre ?: "Un jugador"

                    if (partido != null && partido.creadorId != uid) {
                        sendNotification(
                            userId = partido.creadorId,
                            title = "Nuevo jugador anotado",
                            message = "$playerName se sumó a tu partido: ${partido.titulo}.",
                            type = "new_player"
                        )
                    }
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
                // Buscamos el partido ANTES de la operación
                val partido = _partidos.value.find { it.id == partidoId }
                
                val result = partidoRepository.leavePartido(partidoId, uid)
                if (result.isSuccess) {
                    val userResult = userRepository.getUsersProfiles(listOf(uid))
                    val playerName = userResult.getOrNull()?.firstOrNull()?.nombre ?: "Un jugador"

                    if (partido != null && partido.creadorId != uid) {
                        sendNotification(
                            userId = partido.creadorId,
                            title = "Jugador se bajó",
                            message = "$playerName se bajó de tu partido: ${partido.titulo}.",
                            type = "player_left"
                        )
                    }
                    _uiState.value = PartidoUiState.Success
                } else {
                    _uiState.value = PartidoUiState.Error("Error al bajarse del partido")
                }
            }
        }
    }

    fun finalizarPartido(partidoId: String) {
        viewModelScope.launch {
            _uiState.value = PartidoUiState.Loading
            val partido = _partidos.value.find { it.id == partidoId }
            
            // Ponemos un resultado de prueba realista (ej 3-2 como en la imagen)
            val partidoFinalizado = partido?.copy(
                status = "FINALIZADO",
                golesLocal = 3,
                golesVisitante = 2
            ) ?: return@launch

            val result = partidoRepository.updatePartido(partidoFinalizado)
            if (result.isSuccess) {
                // Notificar a todos que terminó
                partidoFinalizado.jugadoresConfirmados.forEach { playerUid ->
                    if (playerUid != partidoFinalizado.creadorId) {
                        sendNotification(
                            userId = playerUid,
                            title = "Partido finalizado",
                            message = "El partido ${partidoFinalizado.titulo} ha finalizado. ¡Mirá las estadísticas!",
                            type = "match_finished"
                        )
                    }
                }
                _uiState.value = PartidoUiState.Success
            } else {
                _uiState.value = PartidoUiState.Error("Error al finalizar el partido")
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

    private fun sendNotification(userId: String, title: String, message: String, type: String) {
        val db = FirebaseFirestore.getInstance()
        val notificationData = hashMapOf(
            "userId" to userId,
            "title" to title,
            "message" to message,
            "type" to type,
            "isRead" to false,
            "timestamp" to Timestamp.now()
        )
        db.collection("notifications").add(notificationData)
    }

    // 1. Variable para el partido que estás editando actualmente
    private val _partidoEnEdicion = MutableStateFlow<Partido?>(null)
    val partidoEnEdicion: StateFlow<Partido?> = _partidoEnEdicion.asStateFlow()

    // 2. Función para "cargar" el partido al entrar a la pantalla de edición
    fun setPartidoAEditar(partido: Partido) {
        _partidoEnEdicion.value = partido
    }

    // 3. Modifica la función de creación/edición para ser inteligente
    fun guardarCambios(partido: Partido) {
        // Si tiene ID, es editar, sino es crear
        if (partido.id.isNotEmpty()) {
            actualizarPartido(partido)
        } else {
            // llamar a crearPartido normal
        }
    }
}