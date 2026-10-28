package com.vasquez.ayudacampus

import android.app.Application
import com.vasquez.ayudacampus.data.local.AyudaDatabase
import com.vasquez.ayudacampus.data.local.DatosIniciales
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//se vaz a ejecutar cuando Android inicia la aplicacion
//obitene la base de datos, carga con los datos iniciales en segundo plano y avisa cuando termina
class AyudaApp : Application() {
    private val alcanceApp =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val datosInicialesListo = MutableStateFlow(false)
    val datosInicialesListos: StateFlow<Boolean> =
        datosInicialesListo.asStateFlow()

    override fun onCreate() {
        super.onCreate()
        alcanceApp.launch {
            val database = AyudaDatabase.obtener(this@AyudaApp)
            DatosIniciales.sembrar(database)
            datosInicialesListo.value = true
        }
    }
}