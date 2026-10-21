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

    private val _filtroSeleccionado = MutableStateFlow(FiltroBandeja.TODOS)
    val filtroSeleccionado: StateFlow<FiltroBandeja> = _filtroSeleccionado

    private val _textoBusqueda = MutableStateFlow("")
    val textoBusqueda: StateFlow<String> = _textoBusqueda

    private val _prioridadFiltro = MutableStateFlow<String?>(null)
    val prioridadFiltro: StateFlow<String?> = _prioridadFiltro

    private val _categoriaFiltroId = MutableStateFlow<Int?>(null)
    val categoriaFiltroId: StateFlow<Int?> = _categoriaFiltroId

    val categorias = repo.observarCategorias()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val tickets: StateFlow<List<TicketConDetalle>> =
        combine(
            repo.observarTodosLosTickets(),
            _filtroSeleccionado,
            _textoBusqueda,
            _prioridadFiltro,
            _categoriaFiltroId
        ) { lista, filtro, query, prioridad, categoriaId ->
            lista.filter { detalle ->
                val matchEstado = when (filtro) {
                    FiltroBandeja.TODOS -> true
                    FiltroBandeja.ABIERTOS -> detalle.ticket.estado == "ABIERTO"
                    FiltroBandeja.EN_PROCESO -> detalle.ticket.estado == "EN_PROCESO"
                    FiltroBandeja.RESUELTOS -> detalle.ticket.estado == "RESUELTO"
                }
                val matchPrioridad = prioridad == null || detalle.ticket.prioridad.equals(prioridad, ignoreCase = true)
                val matchCategoria = categoriaId == null || detalle.ticket.categoriaId == categoriaId
                val matchTexto = query.isBlank() ||
                        detalle.ticket.titulo.contains(query, ignoreCase = true) ||
                        detalle.ticket.descripcion.contains(query, ignoreCase = true) ||
                        detalle.ticket.ubicacion.contains(query, ignoreCase = true)

                matchEstado && matchPrioridad && matchCategoria && matchTexto
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    fun seleccionarFiltro(filtro: FiltroBandeja) {
        _filtroSeleccionado.value = filtro
    }

    fun actualizarTextoBusqueda(texto: String) {
        _textoBusqueda.value = texto
    }

    fun seleccionarPrioridad(prioridad: String?) {
        _prioridadFiltro.value = prioridad
    }

    fun seleccionarCategoria(categoriaId: Int?) {
        _categoriaFiltroId.value = categoriaId
    }
}