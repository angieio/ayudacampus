package com.vasquez.ayudacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vasquez.ayudacampus.data.prefs.PreferenciasStore
import com.vasquez.ayudacampus.ui.navigation.NavAyuda
import com.vasquez.ayudacampus.ui.theme.AyudaCampusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val preferencias = remember {
                PreferenciasStore(applicationContext)
            }
            val modoOscuro by preferencias.modoOscuro.collectAsStateWithLifecycle(
                initialValue = false
            )

            AyudaCampusTheme(darkTheme = modoOscuro) {
                NavAyuda(preferencias)
            }
        }
    }
}
