package com.vasquez.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vasquez.ayudacampus.data.prefs.PreferenciasStore
import com.vasquez.ayudacampus.data.repository.AyudaRepository

class AyudaViewModelFactory(
    private val repo: AyudaRepository,
    private val preferencias: PreferenciasStore,
    private val usuarioId: Int? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SesionViewModel::class.java)) {
            return SesionViewModel(repo, preferencias) as T
        }

        if (
            modelClass.isAssignableFrom(MisTicketsViewModel::class.java) &&
            usuarioId != null
        ) {
            return MisTicketsViewModel(repo, usuarioId) as T
        }

        if (
            modelClass.isAssignableFrom(NuevoTicketViewModel::class.java) &&
            usuarioId != null
        ) {
            return NuevoTicketViewModel(repo, usuarioId) as T
        }

        if (
            modelClass.isAssignableFrom(NuevoTicketViewModel::class.java) &&
            usuarioId != null
        ) {
            return NuevoTicketViewModel(repo, usuarioId) as T
        }

        throw IllegalArgumentException("ViewModel desconocido o faltan sus datos")
    }
}