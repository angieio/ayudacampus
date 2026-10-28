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

class ForgotViewModel(
    private val repo: AyudaRepository,
    private val preferencias: PreferenciasStore
) : ViewModel() {
    private val _correo = MutableStateFlow("")
    val correo = _correo.asStateFlow()

    private val _nuevaClave = MutableStateFlow("")
    val nuevaClave = _nuevaClave.asStateFlow()

    private val _paso = MutableStateFlow(1) // 1 = verificar correo, 2 = nueva clave
    val paso = _paso.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando = _cargando.asStateFlow()

    private val _exito = MutableSharedFlow<Unit>()
    val exito = _exito.asSharedFlow()

    private var usuarioIdEncontrado: Int? = null

    fun cambiarCorreo(v: String) { _correo.value = v; _error.value = null }
    fun cambiarNuevaClave(v: String) { _nuevaClave.value = v; _error.value = null }

    fun verificarCorreo() {
        val c = _correo.value.trim()
        if (c.isEmpty()) {
            _error.value = "Ingresa tu correo"
            return
        }
        viewModelScope.launch {
            _cargando.value = true
            try {
                val user = repo.obtenerUsuarioPorCorreo(c)
                if (user != null) {
                    usuarioIdEncontrado = user.id
                    _paso.value = 2
                } else {
                    _error.value = "No existe una cuenta con este correo"
                }
            } catch (e: Exception) {
                _error.value = "Error al verificar correo"
            } finally {
                _cargando.value = false
            }
        }
    }

    fun actualizarContrasena() {
        val cl = _nuevaClave.value.trim()
        val userId = usuarioIdEncontrado
        if (cl.isEmpty()) {
            _error.value = "Ingresa la nueva contraseña"
            return
        }
        if (userId == null) {
            _error.value = "Sesión inválida"
            return
        }
        viewModelScope.launch {
            _cargando.value = true
            try {
                repo.actualizarClaveUsuario(userId, cl)
                val user = repo.obtenerUsuario(userId)
                if (user != null) {
                    preferencias.guardarSesion(Rol.CLIENTE, user.id)
                }
                _exito.emit(Unit)
            } catch (e: Exception) {
                _error.value = "Error al actualizar contraseña"
            } finally {
                _cargando.value = false
            }
        }
    }
}
