package com.vasquez.ayudacampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vasquez.ayudacampus.data.local.entity.Usuario
import com.vasquez.ayudacampus.model.EstadoTicket
import com.vasquez.ayudacampus.ui.components.AppTopBar
import com.vasquez.ayudacampus.ui.components.AyudaButton
import com.vasquez.ayudacampus.viewmodel.MisTicketsViewModel

@Composable
fun PerfilScreen(
    usuario: Usuario,
    vm: MisTicketsViewModel,
    modoOscuro: Boolean,
    notificaciones: Boolean,
    onModoOscuroChange: (Boolean) -> Unit,
    onNotificacionesChange: (Boolean) -> Unit,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit,
    onTicketsClick: () -> Unit,
    onNuevoClick: () -> Unit
) {
    val tickets by vm.tickets.collectAsState()
    val abiertos = tickets.count { it.ticket.estado == EstadoTicket.ABIERTO.name }
    val resueltos = tickets.count { it.ticket.estado == EstadoTicket.RESUELTO.name }
    val fondoTarjeta = if (modoOscuro) Color(0xFF1B2028) else Color(0xFFCFFAFE)
    val textoTarjeta = if (modoOscuro) Color(0xFFF3F4F6) else Color(0xFF1F2937)
    val acentoCian = if (modoOscuro) Color(0xFF67D9E8) else Color(0xFF0891B2)

    Scaffold(
        topBar = {
            AppTopBar(titulo = "Perfil")
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(false, onTicketsClick, icon = { Icon(Icons.Default.ConfirmationNumber, null) }, label = { Text("Tickets") })
                NavigationBarItem(false, onNuevoClick, icon = { Text("＋") }, label = { Text("Nuevo") })
                NavigationBarItem(true, {}, icon = { Icon(Icons.Default.Person, null) }, label = { Text("Perfil") })
            }
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(horizontal = 18.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = fondoTarjeta)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, null, tint = acentoCian)
                    Column(Modifier.padding(start = 12.dp)) {
                        Text(usuario.nombre, color = textoTarjeta, fontWeight = FontWeight.Bold)
                        Text(usuario.correo, color = textoTarjeta, fontSize = 12.sp)
                        Text("Estudiante", color = acentoCian, fontSize = 12.sp)
                    }
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ContadorPerfil("$abiertos", "Abiertos", Color(0xFFFFF2C4), Modifier.weight(1f), modoOscuro)
                ContadorPerfil("$resueltos", "Resueltos", Color(0xFFDCFCE7), Modifier.weight(1f), modoOscuro)
                ContadorPerfil("${tickets.size}", "Total", Color(0xFFCFFAFE), Modifier.weight(1f), modoOscuro)
            }
            AyudaButton(
                texto = "Cambiar de perfil",
                onClick = onCambiarPerfil,
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color(0xFF0891B2),
                contentColor = Color.White,
                shape = RoundedCornerShape(14.dp),
                contenido = {
                    Icon(Icons.Default.SwapHoriz, contentDescription = null)
                    Column(
                        Modifier
                            .weight(1f)
                            .padding(start = 10.dp)
                    ) {
                        Text("Cambiar de perfil")
                        Text(
                            "Modo práctico · ir a Soporte TI",
                            fontSize = 11.sp
                        )
                    }
                    Text("›")
                }
            )
            Text("Preferencias", fontWeight = FontWeight.Bold)
            Card(Modifier.fillMaxWidth()) {
                Column {
                    PreferenciaFila("Modo oscuro", "Cambia el tema de la app", modoOscuro, onModoOscuroChange)
                    PreferenciaFila("Notificaciones", "Avisar cambios de estado", notificaciones, onNotificacionesChange)
                }
            }
            Text("Se guardan en el dispositivo con DataStore", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
            Spacer(Modifier.weight(1f))
            OutlinedButton(onClick = onCerrarSesion, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Text("Cerrar sesión")
            }
        }
    }
}

@Composable
private fun ContadorPerfil(valor: String, etiqueta: String, fondo: Color, modifier: Modifier, modoOscuro: Boolean) {
    val fondoFinal = if (modoOscuro) Color(0xFF1B2028) else fondo
    val textoFinal = if (modoOscuro) Color(0xFFF3F4F6) else Color(0xFF1F2937)
    Card(modifier, colors = CardDefaults.cardColors(containerColor = fondoFinal)) {
        Column(Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(valor, color = textoFinal, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(etiqueta, color = textoFinal, fontSize = 11.sp)
        }
    }
}

@Composable
private fun PreferenciaFila(titulo: String, detalle: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(titulo, fontWeight = FontWeight.SemiBold)
            Text(detalle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onChange)
    }
}
