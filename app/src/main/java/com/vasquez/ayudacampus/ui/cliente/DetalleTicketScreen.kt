package com.vasquez.ayudacampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vasquez.ayudacampus.data.local.relation.TicketConDetalle
import com.vasquez.ayudacampus.model.EstadoTicket
import com.vasquez.ayudacampus.ui.components.AppTopBar
import com.vasquez.ayudacampus.ui.components.AyudaButton
import com.vasquez.ayudacampus.ui.components.BurbujaComentario
import com.vasquez.ayudacampus.ui.components.EstadoChip
import com.vasquez.ayudacampus.ui.components.Estrellas
import com.vasquez.ayudacampus.ui.components.PrioridadChip
import com.vasquez.ayudacampus.ui.components.StepperTicket
import com.vasquez.ayudacampus.viewmodel.TicketDetalleViewModel

private val CianDetalle = Color(0xFF0891B2)

@Composable
fun DetalleTicketScreen(
    vm: TicketDetalleViewModel,
    onVolver: () -> Unit,
    onTicketsClick: () -> Unit,
    onNuevoClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
    val detalle by vm.ticket.collectAsState()

    var textoComentario by remember {
        mutableStateOf("")
    }
    var calificacion by remember(detalle?.ticket?.id) {
        mutableIntStateOf(detalle?.ticket?.calificacion ?: 0)
    }
    var comentarioFinal by remember {
        mutableStateOf("")
    }

    if (detalle == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text("Cargando ticket...")
        }
        return
    }

    val ticketDetalle = detalle!!
    val estado = EstadoTicket.values().firstOrNull {
        it.name == ticketDetalle.ticket.estado
    } ?: EstadoTicket.ABIERTO

    val esResuelto = estado == EstadoTicket.RESUELTO
    val esCerrado = estado == EstadoTicket.CERRADO
    val mostrarComposer = !esCerrado && !esResuelto

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    selected = true,
                    onClick = onTicketsClick,
                    icon = {
                        Icon(
                            Icons.Default.ConfirmationNumber,
                            contentDescription = null
                        )
                    },
                    label = { Text("Tickets") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNuevoClick,
                    icon = {
                        Icon(Icons.Default.Add, contentDescription = null)
                    },
                    label = { Text("Nuevo") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onPerfilClick,
                    icon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            AppTopBar(
                titulo = "Ticket #${ticketDetalle.ticket.id.toString().padStart(4, '0')}",
                onVolver = onVolver
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 18.dp,
                    end = 18.dp,
                    top = 8.dp,
                    bottom = 14.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    ResumenTicket(detalle = ticketDetalle)
                }

                if (!esResuelto && !esCerrado) {
                    item {
                        StepperTicket(estado = estado)
                    }
                }

                if (esResuelto) {
                    item {
                        AvisoResuelto()
                    }
                }

                item {
                    Text(
                        text = "Conversación",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                items(
                    items = ticketDetalle.comentarios,
                    key = { it.id }
                ) { comentario ->
                    BurbujaComentario(
                        comentario = comentario,
                        esPropio = comentario.autorId ==
                                ticketDetalle.cliente.id
                    )
                }

                if (esResuelto) {
                    item {
                        CalificacionCard(
                            calificacion = calificacion,
                            onSeleccionar = { calificacion = it }
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = comentarioFinal,
                            onValueChange = { comentarioFinal = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            label = { Text("Comentario final (opcional)") },
                            minLines = 2,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    }

                    item {
                        AyudaButton(
                            texto = "Confirmar y cerrar",
                            onClick = { vm.cerrar(calificacion, comentarioFinal) },
                            enabled = calificacion in 1..5,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            containerColor = CianDetalle,
                            contentColor = Color.White,
                            shape = RoundedCornerShape(14.dp)
                        )
                    }

                    item {
                        OutlinedButton(
                            onClick = vm::reabrir,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = "Reabrir ticket",
                                color = Color(0xFFDC2626)
                            )
                        }
                    }
                }

                if (esCerrado) {
                    item {
                        CalificacionCard(
                            calificacion = ticketDetalle.ticket.calificacion ?: 0,
                            onSeleccionar = {},
                            habilitada = false
                        )
                    }
                }
            }

            if (mostrarComposer) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp,
                            bottom = 10.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = textoComentario,
                        onValueChange = { textoComentario = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(28.dp),
                        placeholder = { Text("Escribe un comentario...") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )

                    IconButton(
                        onClick = {
                            vm.comentar(textoComentario)
                            textoComentario = ""
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(CianDetalle, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Enviar comentario",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ResumenTicket(detalle: TicketConDetalle) {
    val estado = EstadoTicket.values().firstOrNull {
        it.name == detalle.ticket.estado
    } ?: EstadoTicket.ABIERTO

    val colorEstado = when (estado) {
        EstadoTicket.ABIERTO -> Color(0xFFD97706)
        EstadoTicket.EN_PROCESO -> Color(0xFF2563EB)
        EstadoTicket.RESUELTO -> Color(0xFF16A34A)
        EstadoTicket.CERRADO -> Color(0xFF6B7280)
    }

    val colorPrioridad = when (detalle.ticket.prioridad) {
        "ALTA" -> Color(0xFFDC2626)
        "MEDIA" -> Color(0xFFB45309)
        else -> Color(0xFF6B7280)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(15.dp)) {
            Text(
                text = detalle.ticket.titulo,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                EstadoChip(
                    estado = estado.name,
                    fondoPersonalizado = colorEstado.copy(alpha = 0.12f),
                    colorTextoPersonalizado = colorEstado
                )

                PrioridadChip(
                    prioridad = detalle.ticket.prioridad,
                    fondoPersonalizado = colorPrioridad.copy(alpha = 0.12f),
                    colorTextoPersonalizado = colorPrioridad
                )
            }

            Row(
                modifier = Modifier.padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconoCategoria(detalle.categoria.icono)

                Text(
                    text = "${detalle.categoria.nombre} · ${detalle.ticket.ubicacion}",
                    modifier = Modifier.padding(start = 6.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun AvisoResuelto() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFDCFCE7),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "✓",
                color = Color(0xFF16A34A),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Column(Modifier.padding(start = 10.dp)) {
                Text(
                    text = "Soporte marcó este ticket como resuelto.",
                    color = Color(0xFF166534),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = "¿El problema se solucionó?",
                    color = Color(0xFF166534),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun CalificacionCard(
    calificacion: Int,
    onSeleccionar: (Int) -> Unit,
    habilitada: Boolean = true
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (habilitada) "Califica la atención" else "Calificación recibida",
                fontWeight = FontWeight.Bold
            )

            Estrellas(
                seleccion = calificacion,
                onSeleccionar = onSeleccionar,
                habilitadas = habilitada
            )

            Text(
                text = if (calificacion == 0) {
                    "Selecciona de 1 a 5 estrellas"
                } else {
                    "$calificacion de 5 estrellas"
                },
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun IconoCategoria(icono: String) {
    val imagen = when (icono.lowercase()) {
        "wifi" -> Icons.Default.Wifi
        "pc" -> Icons.Default.Computer
        "code" -> Icons.Default.Code
        "key" -> Icons.Default.Key
        "printer" -> Icons.Default.Print
        else -> Icons.Default.Category
    }

    Icon(
        imageVector = imagen,
        contentDescription = null,
        tint = CianDetalle,
        modifier = Modifier.size(16.dp)
    )
}




