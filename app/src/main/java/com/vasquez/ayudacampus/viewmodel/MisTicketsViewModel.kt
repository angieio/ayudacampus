package com.vasquez.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vasquez.ayudacampus.data.local.relation.TicketConDetalle
import com.vasquez.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

enum class FiltroTickets(val etiqueta: String) {
    TODOS("Todos"),
    ABIERTOS("Abiertos"),
    EN_PROCESO("En proceso"),
    RESUELTOS("Resueltos")
}

class MisTicketsViewModel(
    repo: AyudaRepository,
    private val usuarioId: Int
) : ViewModel() {

    private val todosLosTickets = repo.observarTicketsCliente(usuarioId)

    private val _filtroSeleccionado = MutableStateFlow(FiltroTickets.TODOS)
    val filtroSeleccionado: StateFlow<FiltroTickets> =
        _filtroSeleccionado.asStateFlow()

    private val _textoBusqueda = MutableStateFlow("")
    val textoBusqueda: StateFlow<String> = _textoBusqueda.asStateFlow()

    val tickets: StateFlow<List<TicketConDetalle>> =
        combine(todosLosTickets, _filtroSeleccionado, _textoBusqueda) {
                lista, filtro, busqueda ->

            val numeroBuscado = busqueda.trim()
                .removePrefix("#")
                .trim()

            lista.filter { detalle ->
                val ticket = detalle.ticket

                val coincideEstado = when (filtro) {
                    FiltroTickets.TODOS -> true
                    FiltroTickets.ABIERTOS -> ticket.estado == "ABIERTO"
                    FiltroTickets.EN_PROCESO -> ticket.estado == "EN_PROCESO"
                    FiltroTickets.RESUELTOS -> ticket.estado == "RESUELTO"
                }

                val numeroNormal = ticket.id.toString()
                val numeroConCeros = numeroNormal.padStart(4, '0')

                val coincideBusqueda =
                    busqueda.isBlank() ||
                            ticket.titulo.contains(busqueda.trim(), ignoreCase = true) ||
                            numeroNormal.contains(numeroBuscado) ||
                            numeroConCeros.contains(numeroBuscado)

                coincideEstado && coincideBusqueda
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val cantidadAbiertos: StateFlow<Int> =
        todosLosTickets
            .map { lista ->
                lista.count { it.ticket.estado == "ABIERTO" }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0
            )

    fun seleccionarFiltro(filtro: FiltroTickets) {
        _filtroSeleccionado.value = filtro
    }

    fun cambiarBusqueda(texto: String) {
        _textoBusqueda.value = texto
    }
}