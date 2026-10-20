package com.vasquez.ayudacampus.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.vasquez.ayudacampus.model.Rol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//declaracion, se hace una sola vez y a nivel archivo
val Context.dataStore by preferencesDataStore(name = "ajustes")

private object PreferenciasKeys {
    val perfilActivo = stringPreferencesKey("perfilActivo")
    val usuarioId = intPreferencesKey("usuarioId")
    val modoOscuro = booleanPreferencesKey("modoOscuro")
    val notificaciones = booleanPreferencesKey("notificaciones")
    val recibirTickets = booleanPreferencesKey("recibirTickets")
}

class PreferenciasStore (private val context: Context) {
    val perfilActivo: Flow<String> = context.dataStore.data.map { preferencias ->
        preferencias[PreferenciasKeys.perfilActivo] ?: ""
    }

    val usuarioId: Flow<Int> = context.dataStore.data.map { preferencias ->
        preferencias[PreferenciasKeys.usuarioId] ?: 0
    }

    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { preferencias ->
        preferencias[PreferenciasKeys.modoOscuro] ?: false
    }

    val notificaciones: Flow<Boolean> = context.dataStore.data.map { preferencias ->
        preferencias[PreferenciasKeys.notificaciones] ?: true
    }

    val recibirTickets: Flow<Boolean> = context.dataStore.data.map { preferencias ->
        preferencias[PreferenciasKeys.recibirTickets] ?: true
    }

    suspend fun guardarSesion(rol: Rol, usuarioId: Int) {
        context.dataStore.edit { preferencias ->
            preferencias[PreferenciasKeys.perfilActivo] = rol.name
            preferencias[PreferenciasKeys.usuarioId] = usuarioId
        }
    }

    suspend fun guardarModoOscuro(b: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[PreferenciasKeys.modoOscuro] = b
        }
    }

    suspend fun guardarNotificaciones(b: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[PreferenciasKeys.notificaciones] = b
        }
    }

    suspend fun guardarRecibirTickets(b: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[PreferenciasKeys.recibirTickets] = b
        }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit { preferencias ->
            preferencias.remove(PreferenciasKeys.perfilActivo)
            preferencias.remove(PreferenciasKeys.usuarioId)
        }
    }

}