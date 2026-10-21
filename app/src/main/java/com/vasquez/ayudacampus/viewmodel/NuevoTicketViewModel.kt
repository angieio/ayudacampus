package com.vasquez.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vasquez.ayudacampus.data.local.entity.Categoria
import com.vasquez.ayudacampus.data.repository.AyudaRepository
import com.vasquez.ayudacampus.model.Prioridad
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NuevoTicketViewModel(
    private val repo: AyudaRepository,
    private val clienteId: Int
) : ViewModel() {

    val categorias = repo.observarCategoriasActivas().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val _categoria = MutableStateFlow<Categoria?>(null)
    val categoria = _categoria.asStateFlow()

    private val _titulo = MutableStateFlow("")
    val titulo = _titulo.asStateFlow()

    private val _ubicacion = MutableStateFlow("")
    val ubicacion = _ubicacion.asStateFlow()

    private val _descripcion = MutableStateFlow("")
    val descripcion = _descripcion.asStateFlow()

    private val _prioridad = MutableStateFlow(Prioridad.MEDIA)
    val prioridad = _prioridad.asStateFlow()

    private val _errorCategoria = MutableStateFlow<String?>(null)
    val errorCategoria = _errorCategoria.asStateFlow()

    private val _errorTitulo = MutableStateFlow<String?>(null)
    val errorTitulo = _errorTitulo.asStateFlow()

    private val _errorDescripcion = MutableStateFlow<String?>(null)
    val errorDescripcion = _errorDescripcion.asStateFlow()

    private val _errorGeneral = MutableStateFlow<String?>(null)
    val errorGeneral = _errorGeneral.asStateFlow()

    private val _enviando = MutableStateFlow(false)
    val enviando = _enviando.asStateFlow()

    private val _ticketCreado = MutableSharedFlow<Unit>()
    val ticketCreado = _ticketCreado.asSharedFlow()

    fun seleccionarCategoria(valor: Categoria) {
        _categoria.value = valor
        _errorCategoria.value = null
    }

    fun cambiarTitulo(valor: String) {
        _titulo.value = valor
        if (valor.trim().length >= 5) _errorTitulo.value = null
    }

    fun cambiarUbicacion(valor: String) {
        _ubicacion.value = valor
    }

    fun cambiarDescripcion(valor: String) {
        _descripcion.value = valor
        if (valor.trim().length >= 10) _errorDescripcion.value = null
    }

    fun seleccionarPrioridad(valor: Prioridad) {
        _prioridad.value = valor
    }

    fun enviar() {
        val tituloLimpio = _titulo.value.trim()
        val descripcionLimpia = _descripcion.value.trim()
        val categoriaElegida = _categoria.value

        _errorCategoria.value =
            if (categoriaElegida == null) "Elige una categoría" else null

        _errorTitulo.value =
            if (tituloLimpio.length < 5) {
                "El título debe tener al menos 5 caracteres"
            } else {
                null
            }

        _errorDescripcion.value =
            if (descripcionLimpia.length < 10) {
                "La descripción debe tener al menos 10 caracteres"
            } else {
                null
            }

        _errorGeneral.value = null

        if (
            categoriaElegida == null ||
            _errorTitulo.value != null ||
            _errorDescripcion.value != null ||
            _enviando.value
        ) {
            return
        }

        viewModelScope.launch {
            _enviando.value = true

            try {
                repo.crearTicket(
                    clienteId = clienteId,
                    categoriaId = categoriaElegida.id,
                    titulo = tituloLimpio,
                    descripcion = descripcionLimpia,
                    ubicacion = _ubicacion.value.trim(),
                    prioridad = _prioridad.value
                )
                _ticketCreado.emit(Unit)
            } catch (e: Exception) {
                _errorGeneral.value = "No se pudo crear el ticket. Inténtalo de nuevo."
            } finally {
                _enviando.value = false
            }
        }
    }
}