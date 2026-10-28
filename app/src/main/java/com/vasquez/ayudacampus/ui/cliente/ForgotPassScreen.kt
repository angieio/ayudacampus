package com.vasquez.ayudacampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vasquez.ayudacampus.ui.components.AyudaTextField
import com.vasquez.ayudacampus.viewmodel.ForgotViewModel

private val ColorEstudiante = Color(0xFF0799B4)

@Composable
fun ForgotPassScreen(
    vm: ForgotViewModel,
    onVolver: () -> Unit,
    onActualizado: () -> Unit
) {
    val correo by vm.correo.collectAsState()
    val nuevaClave by vm.nuevaClave.collectAsState()
    val paso by vm.paso.collectAsState()
    val error by vm.error.collectAsState()
    val cargando by vm.cargando.collectAsState()

    LaunchedEffect(vm) {
        vm.exito.collect {
            onActualizado()
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
                text = "Recuperar contraseña",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        if (paso == 1) {
            Text("Ingresa tu correo electrónico registrado para verificar tu cuenta.", color = Color.Gray)
            Spacer(Modifier.height(16.dp))
            AyudaTextField(
                value = correo,
                onValueChange = vm::cambiarCorreo,
                label = "Correo",
                modifier = Modifier.fillMaxWidth(),
                icono = Icons.Default.Email,
                color = ColorEstudiante
            )

            if (error != null) {
                Spacer(Modifier.height(8.dp))
                Text(error.orEmpty(), color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = vm::verificarCorreo,
                enabled = !cargando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ColorEstudiante)
            ) {
                Text(if (cargando) "Verificando..." else "Siguiente", fontWeight = FontWeight.Bold)
            }
        } else {
            Text("Ingresa tu nueva contraseña.", color = Color.Gray)
            Spacer(Modifier.height(16.dp))
            AyudaTextField(
                value = nuevaClave,
                onValueChange = vm::cambiarNuevaClave,
                label = "Nueva contraseña",
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
                onClick = vm::actualizarContrasena,
                enabled = !cargando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ColorEstudiante)
            ) {
                Text(if (cargando) "Actualizando..." else "Actualizar contraseña", fontWeight = FontWeight.Bold)
            }
        }
    }
}
