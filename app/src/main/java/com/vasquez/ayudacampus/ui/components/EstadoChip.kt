package com.vasquez.ayudacampus.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EstadoChip(
    estado: String,
    modifier: Modifier = Modifier,
    fondoPersonalizado: Color? = null,
    colorTextoPersonalizado: Color? = null
) {
    val estadoNormalizado = estado.trim().uppercase()

    val (texto, fondo, colorTexto) = when (estadoNormalizado) {
        "ABIERTO" -> Triple("Abierto", Color(0xFFFFF4D6), Color(0xFFD97706))
        "EN_PROCESO" -> Triple("En proceso", Color(0xFFE8F1FF), Color(0xFF2563EB))
        "RESUELTO" -> Triple("Resuelto", Color(0xFFE4F8EB), Color(0xFF16A34A))
        "CERRADO" -> Triple("Cerrado", Color(0xFFF0F1F3), Color(0xFF6B7280))
        else -> Triple(estado, Color(0xFFF0F1F3), Color(0xFF6B7280))
    }

    val modoOscuro = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val colorFondo = fondoPersonalizado ?: if (modoOscuro) {
        when (estadoNormalizado) {
            "ABIERTO" -> Color(0xFF3B2F15)
            "EN_PROCESO" -> Color(0xFF172B45)
            "RESUELTO" -> Color(0xFF153524)
            else -> Color(0xFF2B3038)
        }
    } else {
        fondo
    }
    val colorContenido = colorTextoPersonalizado ?: if (modoOscuro) {
        when (estadoNormalizado) {
            "ABIERTO" -> Color(0xFFFBBF24)
            "EN_PROCESO" -> Color(0xFF60A5FA)
            "RESUELTO" -> Color(0xFF4ADE80)
            else -> Color(0xFFD1D5DB)
        }
    } else {
        colorTexto
    }

    Surface(
        modifier = modifier,
        color = colorFondo,
        contentColor = colorContenido,
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
