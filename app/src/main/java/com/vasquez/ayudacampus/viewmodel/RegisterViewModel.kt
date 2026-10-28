package com.vasquez.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vasquez.ayudacampus.data.prefs.PreferenciasStore
import com.vasquez.ayudacampus.data.repository.AyudaRepository
import com.vasquez.ayudacampus.model.Rol
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repo: AyudaRepository,
    private val preferencias: PreferenciasStore
) : ViewModel() {
    private val _nombre = MutableStateFlow("")
    val nombre = _nombre.asStateFlow()

    private val _correo = MutableStateFlow("")
    val correo = _correo.asStateFlow()

    private val _clave = MutableStateFlow("")
    val clave = _clave.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando = _cargando.asStateFlow()

    private val _registroExitoso = MutableSharedFlow<Unit>()
    val registroExitoso = _registroExitoso.asSharedFlow()

    fun cambiarNombre(v: String) { _nombre.value = v; _error.value = null }
    fun cambiarCorreo(v: String) { _correo.value = v; _error.value = null }
    fun cambiarClave(v: String) { _clave.value = v; _error.value = null }

    fun registrar() {
        val n = _nombre.value.trim()
        val c = _correo.value.trim()
        val cl = _clave.value.trim()

        if (n.isEmpty() || c.isEmpty() || cl.isEmpty()) {
            _error.value = "Completa todos los campos"
            return
        }
        if (!c.contains("@")) {
            _error.value = "Ingresa un correo válido"
            return
        }

        viewModelScope.launch {
            _cargando.value = true
            try {
                repo.registrarUsuario(n, c, cl, Rol.CLIENTE.name)
                val usuario = repo.login(c, cl)
                if (usuario != null) {
                    preferencias.guardarSesion(Rol.CLIENTE, usuario.id)
                }
                _registroExitoso.emit(Unit)
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al registrar usuario"
            } finally {
                _cargando.value = false
            }
        }
    }
}
