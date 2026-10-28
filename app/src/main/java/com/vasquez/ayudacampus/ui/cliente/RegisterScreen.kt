package com.vasquez.ayudacampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vasquez.ayudacampus.ui.components.AyudaTextField
import com.vasquez.ayudacampus.viewmodel.RegisterViewModel

private val ColorEstudiante = Color(0xFF0799B4)

@Composable
fun RegisterScreen(
    vm: RegisterViewModel,
    onVolver: () -> Unit,
    onRegistrado: () -> Unit
) {
    val nombre by vm.nombre.collectAsState()
    val correo by vm.correo.collectAsState()
    val clave by vm.clave.collectAsState()
    val error by vm.error.collectAsState()
    val cargando by vm.cargando.collectAsState()

    LaunchedEffect(vm) {
        vm.registroExitoso.collect {
            onRegistrado()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Crear cuenta de Estudiante",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        AyudaTextField(
            value = nombre,
            onValueChange = vm::cambiarNombre,
            label = "Nombre completo",
            modifier = Modifier.fillMaxWidth(),
            icono = Icons.Default.Person,
            color = ColorEstudiante
        )

        Spacer(Modifier.height(12.dp))

        AyudaTextField(
            value = correo,
            onValueChange = vm::cambiarCorreo,
            label = "Correo institucional",
            modifier = Modifier.fillMaxWidth(),
            icono = Icons.Default.Email,
            color = ColorEstudiante
        )

        Spacer(Modifier.height(12.dp))

        AyudaTextField(
            value = clave,
            onValueChange = vm::cambiarClave,
            label = "Contraseña",
            esClave = true,
            modifier = Modifier.fillMaxWidth(),
            icono = Icons.Default.Lock,
            color = ColorEstudiante
        )

        if (error != null) {
            Spacer(Modifier.height(8.dp))
            Text(error.orEmpty(), color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = vm::registrar,
            enabled = !cargando,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ColorEstudiante)
        ) {
            Text(if (cargando) "Registrando..." else "Registrarse", fontWeight = FontWeight.Bold)
        }
    }
}
