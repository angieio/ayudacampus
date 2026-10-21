package com.vasquez.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vasquez.ayudacampus.data.local.relation.TicketConDetalle
import com.vasquez.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class FiltroBandeja(val etiqueta: String) {
    TODOS("Todos"),
    ABIERTOS("Abiertos"),
    EN_PROCESO("En proceso"),
    RESUELTOS("Resueltos")
}

class BandejaViewModel(
    repo: AyudaRepository
) : ViewModel() {

    private val _filtroSeleccionado =
        MutableStateFlow(FiltroBandeja.TODOS)

    val filtroSeleccionado: StateFlow<FiltroBandeja> =
        _filtroSeleccionado

    val tickets: StateFlow<List<TicketConDetalle>> =
        combine(
            repo.observarTodosLosTickets(),
            _filtroSeleccionado
        ) { lista, filtro ->
            when (filtro) {
                FiltroBandeja.TODOS -> lista
                FiltroBandeja.ABIERTOS ->
                    lista.filter { it.ticket.estado == "ABIERTO" }
                FiltroBandeja.EN_PROCESO ->
                    lista.filter { it.ticket.estado == "EN_PROCESO" }
                FiltroBandeja.RESUELTOS ->
                    lista.filter { it.ticket.estado == "RESUELTO" }
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    fun seleccionarFiltro(filtro: FiltroBandeja) {
        _filtroSeleccionado.value = filtro
    }
}