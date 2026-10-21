package com.vasquez.ayudacampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.VpnKey
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vasquez.ayudacampus.data.local.entity.Categoria
import com.vasquez.ayudacampus.model.Prioridad
import com.vasquez.ayudacampus.ui.components.AyudaButton
import com.vasquez.ayudacampus.viewmodel.NuevoTicketViewModel

private val TurquesaTicket = Color(0xFF0799B4)

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun NuevoTicketScreen(
    vm: NuevoTicketViewModel,
    onVolver: () -> Unit,
    onTicketCreado: () -> Unit,
    onTicketsClick: () -> Unit,
    onPerfilClick: () -> Unit,
    mesaAbierta: Boolean = true
) {
    val categorias by vm.categorias.collectAsState()
    val categoria by vm.categoria.collectAsState()
    val titulo by vm.titulo.collectAsState()
    val ubicacion by vm.ubicacion.collectAsState()
    val descripcion by vm.descripcion.collectAsState()
    val prioridad by vm.prioridad.collectAsState()
    val errorCategoria by vm.errorCategoria.collectAsState()
    val errorTitulo by vm.errorTitulo.collectAsState()
    val errorDescripcion by vm.errorDescripcion.collectAsState()
    val errorGeneral by vm.errorGeneral.collectAsState()
    val enviando by vm.enviando.collectAsState()

    LaunchedEffect(vm) {
        vm.ticketCreado.collect {
            onTicketCreado()
        }
    }

    Scaffold(
        containerColor = Color(0xFFF8FBFD),
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = false,
                    onClick = onTicketsClick,
                    icon = {
                        Icon(Icons.Default.ConfirmationNumber, contentDescription = null)
                    },
                    label = { Text("Tickets") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onVolver,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver"
                    )
                }

                Text(
                    text = "Nuevo ticket",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Text("Categoría", fontWeight = FontWeight.Bold)
            if (!mesaAbierta) {
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    color = Color(0xFFFEE2E2),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Mesa de ayuda cerrada. No se pueden crear tickets por ahora.",
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFFB91C1C)
                    )
                }
            }
            Spacer(Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                categorias.forEach { item ->
                    FilterChip(
                        selected = categoria?.id == item.id,
                        onClick = { vm.seleccionarCategoria(item) },
                        label = {
                            Text(
                                text = item.nombre,
                                textAlign = TextAlign.Center
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = iconoCategoria(item.icono),
                                contentDescription = null
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TurquesaTicket,
                            selectedLabelColor = Color.White,
                            selectedLeadingIconColor = Color.White
                        )
                    )
                }
            }

            if (categorias.isEmpty()) {
                Text(
                    text = "No hay categorías activas",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            errorCategoria?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = titulo,
                onValueChange = vm::cambiarTitulo,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Título") },
                placeholder = { Text("Ej. Sin Wi-Fi en el laboratorio") },
                singleLine = true,
                isError = errorTitulo != null,
                supportingText = {
                    errorTitulo?.let { Text(it) }
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TurquesaTicket,
                    focusedLabelColor = TurquesaTicket
                )
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = ubicacion,
                onValueChange = vm::cambiarUbicacion,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Ubicación") },
                placeholder = { Text("Ej. Lab 3 · Pabellón B") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TurquesaTicket,
                    focusedLabelColor = TurquesaTicket
                )
            )

            Spacer(Modifier.height(16.dp))

            Text("Prioridad", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Prioridad.values().forEach { opcion ->
                    val colorSeleccionado = colorPrioridad(opcion)

                    FilterChip(
                        modifier = Modifier.weight(1f),
                        selected = prioridad == opcion,
                        onClick = { vm.seleccionarPrioridad(opcion) },
                        label = {
                            Text(
                                text = opcion.name.lowercase()
                                    .replaceFirstChar { it.uppercase() }
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = colorSeleccionado,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = vm::cambiarDescripcion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(145.dp),
                label = { Text("Descripción") },
                placeholder = {
                    Text("Describe el problema y cuándo ocurre")
                },
                isError = errorDescripcion != null,
                supportingText = {
                    errorDescripcion?.let { Text(it) }
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TurquesaTicket,
                    focusedLabelColor = TurquesaTicket
                )
            )

            Spacer(Modifier.height(12.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = "Tiempo de respuesta estimado: 4 horas",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            errorGeneral?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(16.dp))

            AyudaButton(
                texto = if (enviando) "Enviando..." else "Enviar ticket",
                onClick = vm::enviar,
                enabled = !enviando && mesaAbierta,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

private fun iconoCategoria(icono: String): ImageVector =
    when (icono.lowercase()) {
        "wifi" -> Icons.Outlined.Wifi
        "pc" -> Icons.Outlined.Computer
        "printer" -> Icons.Outlined.Print
        "key" -> Icons.Outlined.VpnKey
        else -> Icons.Outlined.Code
    }

private fun colorPrioridad(prioridad: Prioridad): Color =
    when (prioridad) {
        Prioridad.BAJA -> Color(0xFF46A96A)
        Prioridad.MEDIA -> Color(0xFFD98719)
        Prioridad.ALTA -> Color(0xFFE3262E)
    }
