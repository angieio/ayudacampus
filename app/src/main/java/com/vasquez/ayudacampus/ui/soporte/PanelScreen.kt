package com.vasquez.ayudacampus.ui.soporte

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vasquez.ayudacampus.ui.components.graficos.GraficoBarras
import com.vasquez.ayudacampus.ui.components.graficos.GraficoDona
import com.vasquez.ayudacampus.viewmodel.PanelViewModel

private val MoradoSoporte = Color(0xFF7C3AED)

@Composable
fun PanelScreen(
    vm: PanelViewModel,
    onTicketsClick: () -> Unit,
    onCategoriasClick: () -> Unit,
    onAjustesClick: () -> Unit,
    onVerAltasClick: () -> Unit
) {
    val abiertos by vm.abiertos.collectAsState()
    val enProceso by vm.enProceso.collectAsState()
    val resueltos by vm.resueltos.collectAsState()
    val porEstado by vm.porEstado.collectAsState()
    val porCategoria by vm.porCategoria.collectAsState()
    val altasSinAtender by vm.altasSinAtender.collectAsState()
    val modoOscuro = MaterialTheme.colorScheme.background.luminance() < 0.5f

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.BarChart, null) },
                    label = { Text("Panel") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onTicketsClick,
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 14.dp,
                bottom = 18.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column {
                    Text(
                        "Panel de soporte",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Mesa de ayuda TI · Hoy",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ContadorSoporte(
                        modifier = Modifier.weight(1f),
                        valor = abiertos,
                        etiqueta = "Abiertos",
                        fondo = Color(0xFFFFF2C4),
                        color = Color(0xFFD97706),
                        modoOscuro = modoOscuro
                    )
                    ContadorSoporte(
                        modifier = Modifier.weight(1f),
                        valor = enProceso,
                        etiqueta = "En proceso",
                        fondo = Color(0xFFDCEAFF),
                        color = Color(0xFF2563EB),
                        modoOscuro = modoOscuro
                    )
                    ContadorSoporte(
                        modifier = Modifier.weight(1f),
                        valor = resueltos,
                        etiqueta = "Resueltos",
                        fondo = Color(0xFFDCFCE7),
                        color = Color(0xFF16A34A),
                        modoOscuro = modoOscuro
                    )
                }
            }

            item {
                Text(
                    "Tickets por estado",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 7.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    GraficoDona(porEstado)
                }
            }

            item {
                Text(
                    "Tickets por categoría",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 7.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    GraficoBarras(porCategoria)
                }
            }

            if (altasSinAtender > 0) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.errorContainer,
                                RoundedCornerShape(16.dp)
                            )
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "⚠",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 22.sp
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp)
                        ) {
                            Text(
                                "$altasSinAtender tickets de prioridad Alta",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Text(
                                "sin atender hace más de 1 hora",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                fontSize = 11.sp
                            )
                        }
                        TextButton(onClick = onVerAltasClick) {
                            Text(
                                "Ver",
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ContadorSoporte(
    modifier: Modifier,
    valor: Int,
    etiqueta: String,
    fondo: Color,
    color: Color,
    modoOscuro: Boolean
) {
    val fondoFinal = if (modoOscuro) MaterialTheme.colorScheme.surfaceVariant else fondo
    val colorFinal = if (modoOscuro) MaterialTheme.colorScheme.onSurface else color
    Column(
        modifier = modifier
            .height(82.dp)
            .background(fondoFinal, RoundedCornerShape(18.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            valor.toString(),
            color = colorFinal,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            etiqueta,
            color = if (modoOscuro) MaterialTheme.colorScheme.onSurfaceVariant else color,
            fontSize = 11.sp
        )
    }
}
