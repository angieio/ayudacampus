package com.vasquez.ayudacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vasquez.ayudacampus.data.prefs.PreferenciasStore
import com.vasquez.ayudacampus.data.repository.AyudaRepository
import com.vasquez.ayudacampus.data.local.entity.Usuario
import com.vasquez.ayudacampus.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repo: AyudaRepository,
    private val preferencias: PreferenciasStore
) : ViewModel() {

    fun establecerUsuario(usuario: Usuario) {
        _usuarioActual.value = usuario
    }

    fun limpiarSesionActual() {
        _usuarioActual.value = null
    }

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val modoOscuro: StateFlow<Boolean> =
        preferencias.modoOscuro.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    fun login(correo: String, clave: String, rolSeleccionado: Rol) {
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Completa el correo y la contraseña."
            return
        }

        viewModelScope.launch {
            _error.value = null
            _usuarioActual.value = null

            try {
                val usuario = repo.login(correo.trim(), clave)

                when {
                    usuario == null -> {
                        _error.value = "Correo o contraseña incorrectos."
                    }

                    usuario.rol != rolSeleccionado.name -> {
                        _error.value = "La cuenta no corresponde al perfil seleccionado."
                    }

                    else -> {
                        preferencias.guardarSesion(rolSeleccionado, usuario.id)
                        _usuarioActual.value = usuario
                    }
                }
            } catch (e: Exception) {
                _error.value = "No se pudo iniciar sesión. Inténtalo de nuevo."
            }
        }
    }

    fun actualizarNombre(nuevoNombre: String) {
        val actual = _usuarioActual.value ?: return
        val nombreLimpio = nuevoNombre.trim()
        if (nombreLimpio.isEmpty()) return

        viewModelScope.launch {
            val actualizado = actual.copy(nombre = nombreLimpio)
            repo.actualizarUsuario(actualizado)
            _usuarioActual.value = actualizado
        }
    }
}
