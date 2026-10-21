package com.vasquez.ayudacampus.ui.soporte

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vasquez.ayudacampus.ui.components.TicketCard
import com.vasquez.ayudacampus.viewmodel.BandejaViewModel
import com.vasquez.ayudacampus.viewmodel.FiltroBandeja

private val MoradoBandeja = Color(0xFF7C3AED)

@Composable
fun BandejaScreen(
    vm: BandejaViewModel,
    onPanelClick: () -> Unit,
    onCategoriasClick: () -> Unit,
    onAjustesClick: () -> Unit,
    onTicketClick: (Int) -> Unit
) {
    val tickets by vm.tickets.collectAsState()
    val filtro by vm.filtroSeleccionado.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = false,
                    onClick = onPanelClick,
                    icon = { Icon(Icons.Default.BarChart, null) },
                    label = { Text("Panel") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.ConfirmationNumber, null) },
                    label = { Text("Tickets") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onCategoriasClick,
                    icon = { Icon(Icons.Default.Category, null) },
                    label = { Text("Categorías") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onAjustesClick,
                    icon = { Icon(Icons.Default.Settings, null) },
                    label = { Text("Ajustes") }
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
            Text(
                text = "Tickets",
                modifier = Modifier.padding(top = 16.dp, bottom = 12.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            androidx.compose.foundation.lazy.LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(FiltroBandeja.values()) { opcion ->
                    val seleccionado = filtro == opcion

                    FilterChip(
                        selected = seleccionado,
                        onClick = { vm.seleccionarFiltro(opcion) },
                        label = { Text(opcion.etiqueta) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            labelColor = Color(0xFF6B7280),
                            selectedContainerColor = MoradoBandeja,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (tickets.isEmpty()) {
                    item {
                        Text(
                            text = "No hay tickets en este filtro.",
                            modifier = Modifier.padding(top = 28.dp),
                            color = Color(0xFF858C99)
                        )
                    }
                }

                items(
                    items = tickets,
                    key = { it.ticket.id }
                ) { detalle ->
                    TicketCard(
                        detalle = detalle,
                        onClick = { onTicketClick(detalle.ticket.id) },
                        mostrarCliente = true
                    )
                }
            }
        }
    }
}