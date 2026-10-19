package com.vasquez.ayudacampus

import android.app.Application
import com.vasquez.ayudacampus.data.local.AyudaDatabase
import com.vasquez.ayudacampus.data.local.DatosIniciales
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AyudaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val database = AyudaDatabase.obtener(this)
        CoroutineScope(Dispatchers.IO).launch {
            DatosIniciales.sembrar(database)
        }
    }
}