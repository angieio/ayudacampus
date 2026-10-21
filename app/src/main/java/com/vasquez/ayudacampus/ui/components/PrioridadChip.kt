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
fun PrioridadChip(
    prioridad: String,
    modifier: Modifier = Modifier
) {
    val prioridadNormalizada = prioridad.trim().uppercase()

    val (texto, fondo, colorTexto) = when (prioridadNormalizada) {
        "ALTA" -> Triple("Alta", Color(0xFFFFE8E8), Color(0xFFDC2626))
        "MEDIA" -> Triple("Media", Color(0xFFFFF0DB), Color(0xFFB45309))
        "BAJA" -> Triple("Baja", Color(0xFFF0F1F3), Color(0xFF6B7280))
        else -> Triple(prioridad, Color(0xFFF0F1F3), Color(0xFF6B7280))
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