package com.vasquez.ayudacampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vasquez.ayudacampus.ui.components.TicketCard
import com.vasquez.ayudacampus.viewmodel.FiltroTickets
import com.vasquez.ayudacampus.viewmodel.MisTicketsViewModel

private val Turquesa = Color(0xFF0799B4)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisTicketsScreen(
    vm: MisTicketsViewModel,
    nombreUsuario: String,
    onTicketClick: (Int) -> Unit,
    onNuevoClick: () -> Unit,
    onPerfilClick: () -> Unit
) {
    val tickets by vm.tickets.collectAsState()
    val filtro by vm.filtroSeleccionado.collectAsState()
    val busqueda by vm.textoBusqueda.collectAsState()
    val cantidadAbiertos by vm.cantidadAbiertos.collectAsState()
    val modoOscuro = MaterialTheme.colorScheme.background.luminance() < 0.5f

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevoClick,
                containerColor = Color(0xFF0891B2),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear ticket")
            }
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(Icons.Default.ConfirmationNumber, contentDescription = null)
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
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Mis tickets",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Hola, $nombreUsuario · $cantidadAbiertos abiertos",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notificaciones"
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            OutlinedTextField(
                value = busqueda,
                onValueChange = vm::cambiarBusqueda,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                placeholder = {
                    Text("Buscar por título o número...")
                },
                leadingIcon = {
                    Text("⌕", color = MaterialTheme.colorScheme.onSurfaceVariant)
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Turquesa,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(FiltroTickets.values().toList()) { opcion ->
                    FilterChip(
                        selected = filtro == opcion,
                        onClick = { vm.seleccionarFiltro(opcion) },
                        label = { Text(opcion.etiqueta) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = if (modoOscuro) Color(0xFF20252D) else MaterialTheme.colorScheme.surface,
                            labelColor = if (modoOscuro) Color(0xFFD1D5DB) else MaterialTheme.colorScheme.onSurfaceVariant,
                            selectedContainerColor = Color(0xFF087F95),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            if (tickets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tienes tickets aquí",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = tickets,
                        key = { it.ticket.id }
                    ) { detalle ->
                        TicketCard(
                            detalle = detalle,
                            onClick = {
                                onTicketClick(detalle.ticket.id)
                            }
                        )
                    }
                }
            }
        }
    }
}
