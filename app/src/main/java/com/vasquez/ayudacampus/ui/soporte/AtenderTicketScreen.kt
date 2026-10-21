package com.vasquez.ayudacampus.ui.soporte

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vasquez.ayudacampus.model.EstadoTicket
import com.vasquez.ayudacampus.model.Prioridad
import com.vasquez.ayudacampus.ui.components.BurbujaComentario
import com.vasquez.ayudacampus.ui.components.EstadoChip
import com.vasquez.ayudacampus.ui.components.PrioridadChip
import com.vasquez.ayudacampus.viewmodel.AtenderViewModel

@Composable
fun AtenderTicketScreen(
    vm: AtenderViewModel,
    onVolver: () -> Unit,
    onPanelClick: () -> Unit,
    onTicketsClick: () -> Unit,
    onCategoriasClick: () -> Unit,
    onAjustesClick: () -> Unit
) {
    val detalle by vm.ticket.collectAsState()
    val estado by vm.estado.collectAsState()
    val prioridad by vm.prioridad.collectAsState()
    val respuesta by vm.respuesta.collectAsState()
    val soporte by vm.soporte.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val cerrado = detalle?.ticket?.estado == EstadoTicket.CERRADO.name

    Scaffold(bottomBar = {
        NavigationBar {
            NavigationBarItem(false, onPanelClick, icon = { Icon(Icons.Default.BarChart, null) }, label = { Text("Panel") })
            NavigationBarItem(true, onTicketsClick, icon = { Icon(Icons.Default.ConfirmationNumber, null) }, label = { Text("Tickets") })
            NavigationBarItem(false, onCategoriasClick, icon = { Icon(Icons.Default.Category, null) }, label = { Text("Categorías") })
            NavigationBarItem(false, onAjustesClick, icon = { Icon(Icons.Default.Settings, null) }, label = { Text("Ajustes") })
        }
    }) { padding ->
        val item = detalle
        if (item == null) {
            Text("Cargando ticket…", Modifier.padding(padding).padding(24.dp))
        } else {
            Column(Modifier.fillMaxSize().padding(padding)) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onVolver) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver") }
                    Text("Atender ticket #${item.ticket.id}", Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Column(Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(item.ticket.titulo, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                            Text("${item.cliente.nombre} · ${item.ticket.ubicacion}", fontSize = 13.sp)
                            Text(item.ticket.descripcion, fontSize = 14.sp)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                EstadoChip(item.ticket.estado)
                                PrioridadChip(item.ticket.prioridad)
                            }
                        }
                        Text("Estado", fontWeight = FontWeight.Bold)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            EstadoTicket.values().forEach { opcion ->
                                FilterChip(
                                    selected = estado == opcion,
                                    onClick = { vm.cambiarEstado(opcion) },
                                    enabled = !cerrado && opcion != EstadoTicket.CERRADO,
                                    label = { Text(opcion.etiquetaCorta(), fontSize = 10.sp) }
                                )
                            }
                        }
                        Text("Prioridad", fontWeight = FontWeight.Bold)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            Prioridad.values().forEach { opcion ->
                                FilterChip(
                                    selected = prioridad == opcion,
                                    onClick = { vm.cambiarPrioridad(opcion) },
                                    enabled = !cerrado,
                                    label = { Text(opcion.etiqueta(), fontSize = 11.sp) }
                                )
                            }
                        }
                        Text("Conversación", fontWeight = FontWeight.Bold)
                    }
                    items(item.comentarios, key = { it.id }) { comentario ->
                        BurbujaComentario(
                            comentario,
                            esPropio = comentario.autorId == soporte?.id,
                            colorPropio = Color(0xFF7C3AED)
                        )
                    }
                    item {
                        mensaje?.let { Text(it) }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = respuesta,
                                onValueChange = vm::actualizarRespuesta,
                                modifier = Modifier.weight(1f),
                                enabled = !cerrado,
                                placeholder = { Text("Responder al cliente…") },
                                shape = RoundedCornerShape(28.dp),
                                maxLines = 3
                            )
                            IconButton(onClick = { vm.responder() }, enabled = !cerrado) {
                                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar")
                            }
                        }
                        Button(
                            onClick = vm::guardar,
                            enabled = !cerrado,
                            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) { Text("Guardar cambios") }
                    }
                }
            }
        }
    }
    LaunchedEffect(vm) { vm.guardado.collect { onVolver() } }
}

private fun EstadoTicket.etiquetaCorta() = when (this) {
    EstadoTicket.ABIERTO -> "Abierto"
    EstadoTicket.EN_PROCESO -> "En proceso"
    EstadoTicket.RESUELTO -> "Resuelto"
    EstadoTicket.CERRADO -> "Cerrado"
}

private fun Prioridad.etiqueta() = name.lowercase().replaceFirstChar { it.uppercase() }
