package com.vasquez.ayudacampus.ui.soporte

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Monitor
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.border
import androidx.compose.ui.unit.dp
import com.vasquez.ayudacampus.data.local.entity.Categoria
import com.vasquez.ayudacampus.viewmodel.CategoriasViewModel

@Composable
fun FormularioCategoriaScreen(
    vm: CategoriasViewModel,
    categoriaId: Int?,
    onVolver: () -> Unit
) {
    val categorias by vm.categorias.collectAsState()
    var nombre by remember(categoriaId) { mutableStateOf("") }
    var icono by remember(categoriaId) { mutableStateOf("tag") }
    var activa by remember(categoriaId) { mutableStateOf(true) }
    var cantidadTickets by remember(categoriaId) { mutableStateOf(0) }
    var error by remember { mutableStateOf<String?>(null) }
    val editada = categorias.firstOrNull { it.id == categoriaId }

    LaunchedEffect(editada) {
        if (editada != null) {
            nombre = editada.nombre
            icono = editada.icono
            activa = editada.activa
        }
    }
    LaunchedEffect(categoriaId) {
        if (categoriaId != null) cantidadTickets = vm.contarTickets(categoriaId)
    }
    LaunchedEffect(vm) { vm.guardado.collect { onVolver() } }

    Scaffold { padding ->
        Column(Modifier.padding(padding).padding(horizontal = 22.dp, vertical = 12.dp)) {
            androidx.compose.foundation.layout.Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                TextButton(
                    onClick = onVolver,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) { Text("‹") }
                Text(if (categoriaId == null) "Nueva categoría" else "Editar categoría")
            }
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it; error = null },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nombre de la categoría") },
                singleLine = true,
                isError = error != null,
                supportingText = { error?.let { Text(it) } }
            )
            Text("Icono", Modifier.padding(top = 12.dp, bottom = 6.dp))
            val iconos = listOf("pc", "code", "wifi", "key", "printer", "tag")
            iconos.chunked(3).forEach { fila ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    fila.forEach { opcion ->
                        Surface(
                            modifier = Modifier.weight(1f).height(62.dp)
                                .border(
                                    width = if (icono == opcion) 2.dp else 1.dp,
                                    color = if (icono == opcion) Color(0xFF7C3AED) else Color(0xFFE5E7EB),
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable { icono = opcion },
                            shape = RoundedCornerShape(14.dp),
                            color = if (icono == opcion) Color(0xFFEDE4FF) else Color.White
                        ) {
                            androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                                Icon(iconoCategoria(opcion), contentDescription = opcion)
                            }
                        }
                    }
                }
            }
            Card(Modifier.fillMaxWidth().padding(top = 14.dp)) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Categoría activa")
                        Text("Los estudiantes podrán elegirla")
                    }
                    Switch(checked = activa, onCheckedChange = { activa = it })
                }
            }
            Text("Vista previa", Modifier.padding(top = 14.dp, bottom = 8.dp))
            Card(Modifier.fillMaxWidth()) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(iconoCategoria(icono), contentDescription = null)
                    Column(Modifier.padding(start = 12.dp)) {
                        Text(nombre.ifBlank { "Nombre de categoría" })
                        Text("$cantidadTickets tickets")
                    }
                }
            }
            Button(
                onClick = {
                    if (nombre.isBlank()) error = "El nombre es obligatorio."
                    else vm.guardar(Categoria(id = categoriaId ?: 0, nombre = nombre, icono = icono, activa = activa))
                },
                modifier = Modifier.fillMaxWidth().padding(top = 18.dp),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Guardar categoría") }
            OutlinedButton(onClick = onVolver, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Text("Cancelar")
            }
        }
    }
}

@Composable
private fun iconoCategoria(valor: String) = when (valor) {
    "pc" -> Icons.Default.Monitor
    "code" -> Icons.Default.Code
    "wifi" -> Icons.Default.Wifi
    "key" -> Icons.Default.Key
    "printer" -> Icons.Default.Print
    else -> Icons.Default.Label
}
