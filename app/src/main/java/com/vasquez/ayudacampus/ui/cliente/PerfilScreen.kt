package com.vasquez.ayudacampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vasquez.ayudacampus.data.local.entity.Usuario
import com.vasquez.ayudacampus.model.EstadoTicket
import com.vasquez.ayudacampus.viewmodel.MisTicketsViewModel

@Composable
fun PerfilScreen(
    usuario: Usuario,
    vm: MisTicketsViewModel,
    modoOscuro: Boolean,
    notificaciones: Boolean,
    onModoOscuroChange: (Boolean) -> Unit,
    onNotificacionesChange: (Boolean) -> Unit,
    onActualizarNombre: (String) -> Unit,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit,
    onTicketsClick: () -> Unit,
    onNuevoClick: () -> Unit
) {
    val tickets by vm.tickets.collectAsState()
    val abiertos = tickets.count { it.ticket.estado == EstadoTicket.ABIERTO.name }
    val resueltos = tickets.count { it.ticket.estado == EstadoTicket.RESUELTO.name }
    val cerradosConCalificacion = tickets.filter { it.ticket.estado == EstadoTicket.CERRADO.name && (it.ticket.calificacion ?: 0) > 0 }

    var mostrarDialogoEditar by remember { mutableStateOf(false) }
    var nombreEdit by remember(usuario.nombre) { mutableStateOf(usuario.nombre) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(false, onTicketsClick, icon = { Icon(Icons.Default.ConfirmationNumber, null) }, label = { Text("Tickets") })
                NavigationBarItem(false, onNuevoClick, icon = { Text("＋") }, label = { Text("Nuevo") })
                NavigationBarItem(true, {}, icon = { Icon(Icons.Default.Person, null) }, label = { Text("Perfil") })
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Perfil",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Card(
                    Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFCFFAFE))
                ) {
                    Row(
                        Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Person, null, tint = Color(0xFF0891B2))
                        Column(Modifier.weight(1f).padding(start = 12.dp)) {
                            Text(usuario.nombre, fontWeight = FontWeight.Bold)
                            Text(usuario.correo, fontSize = 12.sp)
                            Text("Estudiante", color = Color(0xFF0891B2), fontSize = 12.sp)
                        }
                        IconButton(onClick = { mostrarDialogoEditar = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar perfil", tint = Color(0xFF0891B2))
                        }
                    }
                }
            }

            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ContadorPerfil("$abiertos", "Abiertos", Color(0xFFFFF2C4), Modifier.weight(1f))
                    ContadorPerfil("$resueltos", "Resueltos", Color(0xFFDCFCE7), Modifier.weight(1f))
                    ContadorPerfil("${tickets.size}", "Total", Color(0xFFCFFAFE), Modifier.weight(1f))
                }
            }

            if (cerradosConCalificacion.isNotEmpty()) {
                item {
                    Text("Historial de calificaciones dadas", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                items(cerradosConCalificacion) { detalle ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(detalle.ticket.titulo, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            Text(
                                "Calificación: ${detalle.ticket.calificacion} / 5 ⭐",
                                fontSize = 12.sp,
                                color = Color(0xFF0891B2),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = onCambiarPerfil,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.SwapHoriz, null)
                    Column(Modifier.weight(1f).padding(start = 10.dp)) {
                        Text("Cambiar de perfil")
                        Text("Modo práctico · ir a Soporte TI", fontSize = 11.sp)
                    }
                    Text("›")
                }
            }

            item {
                Text("Preferencias", fontWeight = FontWeight.Bold)
                Card(Modifier.fillMaxWidth()) {
                    Column {
                        PreferenciaFila("Modo oscuro", "Cambia el tema de la app", modoOscuro, onModoOscuroChange)
                        PreferenciaFila("Notificaciones", "Avisar cambios de estado", notificaciones, onNotificacionesChange)
                    }
                }
            }

            item {
                Text("Se guardan en el dispositivo con DataStore", color = Color.Gray, fontSize = 12.sp)
            }

            item {
                Spacer(Modifier.height(16.dp))
                OutlinedButton(
                    onClick = onCerrarSesion,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cerrar sesión")
                }
            }
        }
    }

    if (mostrarDialogoEditar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEditar = false },
            title = { Text("Editar nombre de perfil") },
            text = {
                OutlinedTextField(
                    value = nombreEdit,
                    onValueChange = { nombreEdit = it },
                    label = { Text("Nombre") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(onClick = {
                    onActualizarNombre(nombreEdit)
                    mostrarDialogoEditar = false
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { mostrarDialogoEditar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun ContadorPerfil(valor: String, etiqueta: String, fondo: Color, modifier: Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = fondo)) {
        Column(Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(valor, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(etiqueta, fontSize = 11.sp)
        }
    }
}

@Composable
private fun PreferenciaFila(titulo: String, detalle: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(titulo, fontWeight = FontWeight.SemiBold)
            Text(detalle, fontSize = 11.sp, color = Color.Gray)
        }
        Switch(checked = checked, onCheckedChange = onChange)
    }
}
