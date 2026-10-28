package com.vasquez.ayudacampus.ui.soporte

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vasquez.ayudacampus.data.local.entity.Usuario
import com.vasquez.ayudacampus.ui.components.AppTopBar
import com.vasquez.ayudacampus.ui.components.AyudaButton

@Composable
fun AjustesScreen(
    usuario: Usuario,
    modoOscuro: Boolean,
    notificaciones: Boolean,
    recibirTickets: Boolean,
    onModoOscuroChange: (Boolean) -> Unit,
    onNotificacionesChange: (Boolean) -> Unit,
    onRecibirTicketsChange: (Boolean) -> Unit,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit,
    onPanelClick: () -> Unit,
    onTicketsClick: () -> Unit,
    onCategoriasClick: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(titulo = "Ajustes")
        },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar {
                NavigationBarItem(false, onPanelClick, icon = { Icon(Icons.Default.BarChart, null) }, label = { Text("Panel") })
                NavigationBarItem(false, onTicketsClick, icon = { Icon(Icons.Default.ConfirmationNumber, null) }, label = { Text("Tickets") })
                NavigationBarItem(false, onCategoriasClick, icon = { Icon(Icons.Default.Category, null) }, label = { Text("Categorías") })
                NavigationBarItem(true, {}, icon = { Icon(Icons.Default.Settings, null) }, label = { Text("Ajustes") })
            }
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(horizontal = 18.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.secondary)
                    Column(Modifier.padding(start = 12.dp)) {
                        Text("Soporte TI", fontWeight = FontWeight.Bold)
                        Text(usuario.correo, fontSize = 12.sp)
                        Text("Perfil activo", color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
                    }
                }
            }
            AyudaButton(
                texto = "Cambiar de perfil",
                onClick = onCambiarPerfil,
                containerColor = Color(0xFF7C3AED),
                contentColor = Color.White,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                contenido = {
                    Icon(Icons.Default.SwapHoriz, contentDescription = null)
                    Column(
                        Modifier
                            .weight(1f)
                            .padding(start = 10.dp)
                    ) {
                        Text("Cambiar de perfil")
                        Text("Modo práctico · ir a Estudiante", fontSize = 11.sp)
                    }
                    Text("›")
                }
            )
            Text("Preferencias", fontWeight = FontWeight.Bold)
            Card(Modifier.fillMaxWidth()) {
                Column {
                    PreferenciaAjuste("Modo oscuro", "Cambiar el tema de la app", modoOscuro, onModoOscuroChange)
                    PreferenciaAjuste("Notificaciones", "Avisar cambios de estado", notificaciones, onNotificacionesChange)
                    PreferenciaAjuste("Recibir tickets", "Abrir o cerrar la mesa de ayuda", recibirTickets, onRecibirTicketsChange)
                }
            }
            Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Column(Modifier.padding(12.dp)) {
                    Text("Se guardan en el dispositivo", fontSize = 12.sp)
                    Text("con DataStore", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
            Spacer(Modifier.weight(1f))
            OutlinedButton(onClick = onCerrarSesion, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Text("Cerrar sesión", color = Color(0xFFDC2626))
            }
        }
    }
}

@Composable
private fun PreferenciaAjuste(titulo: String, detalle: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(titulo, fontWeight = FontWeight.SemiBold)
            Text(detalle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onChange)
    }
}
