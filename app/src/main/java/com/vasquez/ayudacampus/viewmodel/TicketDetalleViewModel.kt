package com.vasquez.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vasquez.ayudacampus.data.local.relation.TicketConDetalle
import com.vasquez.ayudacampus.data.repository.AyudaRepository
import com.vasquez.ayudacampus.model.EstadoTicket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TicketDetalleViewModel(
    private val repo: AyudaRepository,
    private val ticketId: Int
) : ViewModel() {

    private val _ticket = MutableStateFlow<TicketConDetalle?>(null)
    val ticket: StateFlow<TicketConDetalle?> = _ticket.asStateFlow()

    init {
        viewModelScope.launch {
            repo.observarTicket(ticketId).collect { detalle ->
                _ticket.value = detalle
            }
        }
    }

    fun comentar(texto: String) {
        val detalle = _ticket.value ?: return
        val textoLimpio = texto.trim()

        if (textoLimpio.isEmpty()) return
        if (detalle.ticket.estado == EstadoTicket.CERRADO.name) return

        viewModelScope.launch {
            repo.agregarComentario(
                ticketId = ticketId,
                autor = detalle.cliente,
                texto = textoLimpio
            )
        }
    }

    // El comentario final es opcional. Se guarda antes de cerrar.
    fun cerrar(calificacion: Int, comentarioFinal: String = "") {
        val detalle = _ticket.value ?: return

        if (detalle.ticket.estado != EstadoTicket.RESUELTO.name) return
        if (calificacion !in 1..5) return

        viewModelScope.launch {
            val textoFinalLimpio = comentarioFinal.trim()

            if (textoFinalLimpio.isNotEmpty()) {
                repo.agregarComentario(
                    ticketId = ticketId,
                    autor = detalle.cliente,
                    texto = textoFinalLimpio
                )
            }

            repo.cerrarConCalificacion(ticketId, calificacion)
        }
    }

    fun reabrir() {
        val detalle = _ticket.value ?: return
        if (detalle.ticket.estado != EstadoTicket.RESUELTO.name) return

        viewModelScope.launch {
            repo.reabrir(ticketId)
        }
    }
}