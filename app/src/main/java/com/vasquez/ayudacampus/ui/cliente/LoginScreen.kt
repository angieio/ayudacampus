package com.vasquez.ayudacampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vasquez.ayudacampus.model.Rol
import com.vasquez.ayudacampus.ui.components.AyudaButton
import com.vasquez.ayudacampus.ui.components.AyudaTextField
import com.vasquez.ayudacampus.viewmodel.SesionViewModel

private val ColorEstudiante = Color(0xFF0799B4)
private val ColorSoporte = Color(0xFF7838E8)

@Composable
fun LoginScreen(vm: SesionViewModel) {
    var rolSeleccionado by remember { mutableStateOf(Rol.CLIENTE) }
    var correo by remember { mutableStateOf("juan@campus.test") }
    var clave by remember { mutableStateOf("1234") }

    val error by vm.error.collectAsState()
    val colorRol =
        if (rolSeleccionado == Rol.CLIENTE) ColorEstudiante else ColorSoporte

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 36.dp)
    ) {
        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Ingresa para continuar",
            color = Color.Gray
        )

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF0F2F5), RoundedCornerShape(28.dp))
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OpcionRol(
                texto = "Estudiante",
                seleccionado = rolSeleccionado == Rol.CLIENTE,
                color = ColorEstudiante,
                modifier = Modifier.weight(1f)
            ) {
                rolSeleccionado = Rol.CLIENTE
                correo = "juan@campus.test"
                clave = "1234"
            }

            OpcionRol(
                texto = "Soporte TI",
                seleccionado = rolSeleccionado == Rol.ADMIN,
                color = ColorSoporte,
                modifier = Modifier.weight(1f)
            ) {
                rolSeleccionado = Rol.ADMIN
                correo = "soporte@campus.test"
                clave = "admin"
            }
        }

        Spacer(Modifier.height(22.dp))

        AyudaTextField(
            value = correo,
            onValueChange = { correo = it },
            label = "Correo",
            modifier = Modifier.fillMaxWidth(),
            icono = Icons.Default.Email,
            color = colorRol
        )

        Spacer(Modifier.height(12.dp))

        AyudaTextField(
            value = clave,
            onValueChange = { clave = it },
            label = "Contraseña",
            esClave = true,
            modifier = Modifier.fillMaxWidth(),
            icono = Icons.Default.Lock,
            color = colorRol
        )

        if (error != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = error.orEmpty(),
                color = MaterialTheme.colorScheme.error
            )
        }

        Text(
            text = "¿Olvidaste tu contraseña?",
            color = colorRol,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )

        Spacer(Modifier.height(24.dp))

        AyudaButton(
            texto = "Ingresar",
            onClick = { vm.login(correo, clave, rolSeleccionado) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            containerColor = colorRol,
            contentColor = Color.White,
            shape = RoundedCornerShape(14.dp)
        )

        if (rolSeleccionado == Rol.CLIENTE) {
            Text(
                text = "¿No tienes cuenta?  Regístrate",
                color = ColorEstudiante,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp)
                    .background(Color(0xFFF5F0FF), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Modo práctico",
                    color = ColorSoporte,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Puedes cambiar de perfil desde Ajustes para probar ambos roles.",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun OpcionRol(
    texto: String,
    seleccionado: Boolean,
    color: Color,
    modifier: Modifier = Modifier,
    alSeleccionar: () -> Unit
) {
    Box(
        modifier = modifier
            .height(44.dp)
            .background(
                color = if (seleccionado) color else Color.Transparent,
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(onClick = alSeleccionar),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            color = if (seleccionado) Color.White else Color(0xFF566071),
            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
