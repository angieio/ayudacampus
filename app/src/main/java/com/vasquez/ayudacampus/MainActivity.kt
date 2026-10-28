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
import com.vasquez.ayudacampus.ui.cliente.SplashScreen
import com.vasquez.ayudacampus.ui.navigation.NavAyuda
import com.vasquez.ayudacampus.ui.theme.AyudaCampusTheme

//la pantalla principal que android abre.
//mientras los datos cargan, se mostrara el splash, y cuando esten listos, se mostrara la navegacion.
//tambien lee la preferencia del modo oscuro y lo aplica al tema
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val aplicacion = application as AyudaApp
            val datosListos by aplicacion.datosInicialesListos.collectAsStateWithLifecycle()

            val preferencias = remember {
                PreferenciasStore(applicationContext)
            }
            val modoOscuro by preferencias.modoOscuro.collectAsStateWithLifecycle(
                initialValue = false
            )

            AyudaCampusTheme(darkTheme = modoOscuro) {
                if (datosListos) {
                    NavAyuda(preferencias)
                } else {
                    SplashScreen()
                }
            }
        }
    }
}
