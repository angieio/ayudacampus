package com.vasquez.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vasquez.ayudacampus.data.repository.AyudaRepository
import com.vasquez.ayudacampus.model.DatoGrafico
import com.vasquez.ayudacampus.model.EstadoTicket
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PanelViewModel(
    repo: AyudaRepository
) : ViewModel() {

    private val inicioCompartido = SharingStarted.WhileSubscribed(5_000)

    val abiertos: StateFlow<Int> =
        repo.observarCantidadPorEstado(EstadoTicket.ABIERTO)
            .stateIn(viewModelScope, inicioCompartido, 0)

    val enProceso: StateFlow<Int> =
        repo.observarCantidadPorEstado(EstadoTicket.EN_PROCESO)
            .stateIn(viewModelScope, inicioCompartido, 0)

    val resueltos: StateFlow<Int> =
        repo.observarCantidadPorEstado(EstadoTicket.RESUELTO)
            .stateIn(viewModelScope, inicioCompartido, 0)

    private val cerrados: StateFlow<Int> =
        repo.observarCantidadPorEstado(EstadoTicket.CERRADO)
            .stateIn(viewModelScope, inicioCompartido, 0)

    val porEstado: StateFlow<List<DatoGrafico>> =
        combine(abiertos, enProceso, resueltos, cerrados) {
                abiertos, enProceso, resueltos, cerrados ->
            listOf(
                DatoGrafico("Abierto", abiertos, 0xFFD97706),
                DatoGrafico("En proceso", enProceso, 0xFF2563EB),
                DatoGrafico("Resuelto", resueltos, 0xFF16A34A),
                DatoGrafico("Cerrado", cerrados, 0xFF6B7280)
            )
        }.stateIn(viewModelScope, inicioCompartido, emptyList())

    private val coloresCategorias = listOf(
        0xFFC4B5FD,
        0xFFA78BFA,
        0xFF7C3AED,
        0xFFD8B4FE,
        0xFFBFA5F8
    )

    val porCategoria: StateFlow<List<DatoGrafico>> =
        repo.observarCantidadPorCategoria()
            .map { conteos ->
                conteos.mapIndexed { indice, conteo ->
                    DatoGrafico(
                        etiqueta = conteo.categoriaNombre,
                        valor = conteo.total,
                        color = coloresCategorias[indice % coloresCategorias.size]
                    )
                }
            }
            .stateIn(viewModelScope, inicioCompartido, emptyList())

    private val limiteUnaHora = System.currentTimeMillis() - 60 * 60 * 1000L

    val altasSinAtender: StateFlow<Int> =
        repo.observarAltasSinAtender(limiteUnaHora)
            .stateIn(viewModelScope, inicioCompartido, 0)
}