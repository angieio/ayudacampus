package com.vasquez.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vasquez.ayudacampus.data.local.entity.Categoria
import com.vasquez.ayudacampus.data.repository.AyudaRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoriasViewModel(private val repo: AyudaRepository) : ViewModel() {
    val categorias: StateFlow<List<Categoria>> = repo.observarCategorias()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _guardado = Channel<Unit>(Channel.BUFFERED)
    val guardado = _guardado.receiveAsFlow()

    fun guardar(categoria: Categoria) {
        val nombre = categoria.nombre.trim()
        if (nombre.isEmpty()) {
            _mensaje.value = "Escribe el nombre de la categoría."
            return
        }
        viewModelScope.launch {
            repo.guardarCategoria(categoria.copy(nombre = nombre))
            _guardado.send(Unit)
        }
    }

    fun eliminar(categoria: Categoria) {
        viewModelScope.launch {
            if (repo.contarTicketsPorCategoria(categoria.id) > 0) {
                _mensaje.value = "No se puede eliminar: la categoría ya tiene tickets."
            } else {
                repo.eliminarCategoria(categoria)
                _mensaje.value = "Categoría eliminada."
            }
        }
    }

    fun limpiarMensaje() { _mensaje.value = null }

    suspend fun contarTickets(categoriaId: Int): Int = repo.contarTicketsPorCategoria(categoriaId)
}
