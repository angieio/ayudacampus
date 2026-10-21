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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vasquez.ayudacampus.data.local.entity.Categoria
import com.vasquez.ayudacampus.viewmodel.CategoriasViewModel

@Composable
fun CategoriasScreen(
    vm: CategoriasViewModel,
    onPanelClick: () -> Unit,
    onTicketsClick: () -> Unit,
    onAjustesClick: () -> Unit,
    onNuevaClick: () -> Unit,
    onEditarClick: (Int) -> Unit
) {
    val categorias by vm.categorias.collectAsState()
    val snackbar = androidx.compose.runtime.remember { SnackbarHostState() }
    val mensaje by vm.mensaje.collectAsState()
    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        floatingActionButton = { FloatingActionButton(onClick = onNuevaClick) { Icon(Icons.Default.Add, "Nueva categoría") } },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(false, onPanelClick, icon = { Icon(Icons.Default.BarChart, null) }, label = { Text("Panel") })
                NavigationBarItem(false, onTicketsClick, icon = { Icon(Icons.Default.ConfirmationNumber, null) }, label = { Text("Tickets") })
                NavigationBarItem(true, {}, icon = { Icon(Icons.Default.Category, null) }, label = { Text("Categorías") })
                NavigationBarItem(false, onAjustesClick, icon = { Icon(Icons.Default.Settings, null) }, label = { Text("Ajustes") })
            }
        }
    ) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { Text("Categorías", Modifier.padding(bottom = 8.dp)) }
            items(categorias, key = { it.id }) { categoria ->
                Card(Modifier.fillMaxWidth()) {
                    androidx.compose.foundation.layout.Row(
                        Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(categoria.nombre)
                            Text(if (categoria.activa) "Activa" else "Inactiva")
                        }
                        IconButton(onClick = { onEditarClick(categoria.id) }) { Icon(Icons.Default.Edit, "Editar") }
                        IconButton(onClick = { vm.eliminar(categoria) }) { Icon(Icons.Default.Delete, "Eliminar") }
                    }
                }
            }
        }
    }
    LaunchedEffect(mensaje) {
        mensaje?.let { snackbar.showSnackbar(it); vm.limpiarMensaje() }
    }
}
