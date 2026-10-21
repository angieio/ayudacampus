package com.vasquez.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vasquez.ayudacampus.data.local.entity.Usuario
import com.vasquez.ayudacampus.data.local.relation.TicketConDetalle
import com.vasquez.ayudacampus.data.repository.AyudaRepository
import com.vasquez.ayudacampus.model.EstadoTicket
import com.vasquez.ayudacampus.model.Prioridad
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AtenderViewModel(
    private val repo: AyudaRepository,
    private val ticketId: Int,
    private val soporteId: Int
) : ViewModel() {
    val ticket: StateFlow<TicketConDetalle?> = repo.observarTicket(ticketId)
        .map { detalle -> detalle as TicketConDetalle? }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _estado = MutableStateFlow(EstadoTicket.ABIERTO)
    val estado: StateFlow<EstadoTicket> = _estado.asStateFlow()

    private val _prioridad = MutableStateFlow(Prioridad.MEDIA)
    val prioridad: StateFlow<Prioridad> = _prioridad.asStateFlow()

    private val _respuesta = MutableStateFlow("")
    val respuesta: StateFlow<String> = _respuesta.asStateFlow()

    private val _soporte = MutableStateFlow<Usuario?>(null)
    val soporte: StateFlow<Usuario?> = _soporte.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _guardado = Channel<Unit>(Channel.BUFFERED)
    val guardado = _guardado.receiveAsFlow()

    init {
        viewModelScope.launch { _soporte.value = repo.obtenerUsuario(soporteId) }
        viewModelScope.launch {
            ticket.collect { detalle ->
                detalle?.let {
                    _estado.value = runCatching { EstadoTicket.valueOf(it.ticket.estado) }
                        .getOrDefault(EstadoTicket.ABIERTO)
                    _prioridad.value = runCatching { Prioridad.valueOf(it.ticket.prioridad) }
                        .getOrDefault(Prioridad.MEDIA)
                }
            }
        }
    }

    fun cambiarEstado(estado: EstadoTicket) {
        if (estado == EstadoTicket.CERRADO || ticket.value?.ticket?.estado == EstadoTicket.CERRADO.name) return
        _estado.value = estado
    }

    fun cambiarPrioridad(prioridad: Prioridad) {
        if (ticket.value?.ticket?.estado == EstadoTicket.CERRADO.name) return
        _prioridad.value = prioridad
    }

    fun actualizarRespuesta(texto: String) { _respuesta.value = texto }

    fun responder(texto: String = _respuesta.value) {
        val detalle = ticket.value ?: return
        val autor = soporte.value ?: run { _mensaje.value = "No se encontró el usuario de Soporte."; return }
        val limpio = texto.trim()
        if (limpio.isEmpty() || detalle.ticket.estado == EstadoTicket.CERRADO.name) return
        viewModelScope.launch {
            repo.agregarComentario(ticketId, autor, limpio)
            _respuesta.value = ""
        }
    }

    fun guardar() {
        val detalle = ticket.value ?: return
        if (detalle.ticket.estado == EstadoTicket.CERRADO.name) return
        viewModelScope.launch {
            repo.cambiarEstado(ticketId, _estado.value)
            repo.cambiarPrioridad(ticketId, _prioridad.value)
            _guardado.send(Unit)
        }
    }

    fun limpiarMensaje() { _mensaje.value = null }
}
