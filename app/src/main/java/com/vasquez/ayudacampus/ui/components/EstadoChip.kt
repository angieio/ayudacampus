package com.vasquez.ayudacampus.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EstadoChip(
    estado: String,
    modifier: Modifier = Modifier
) {
    val estadoNormalizado = estado.trim().uppercase()

    val (texto, fondo, colorTexto) = when (estadoNormalizado) {
        "ABIERTO" -> Triple("Abierto", Color(0xFFFFF4D6), Color(0xFFD97706))
        "EN_PROCESO" -> Triple("En proceso", Color(0xFFE8F1FF), Color(0xFF2563EB))
        "RESUELTO" -> Triple("Resuelto", Color(0xFFE4F8EB), Color(0xFF16A34A))
        "CERRADO" -> Triple("Cerrado", Color(0xFFF0F1F3), Color(0xFF6B7280))
        else -> Triple(estado, Color(0xFFF0F1F3), Color(0xFF6B7280))
    }

    Surface(
        modifier = modifier,
        color = fondo,
        contentColor = colorTexto,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = texto,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}