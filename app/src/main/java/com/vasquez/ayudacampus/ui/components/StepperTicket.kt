package com.vasquez.ayudacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vasquez.ayudacampus.model.EstadoTicket

@Composable
fun StepperTicket(estado: EstadoTicket) {
    val pasos = listOf("Abierto", "En proceso", "Resuelto", "Cerrado")
    val indiceActivo = when (estado) {
        EstadoTicket.ABIERTO -> 0
        EstadoTicket.EN_PROCESO -> 1
        EstadoTicket.RESUELTO -> 2
        EstadoTicket.CERRADO -> 3
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            pasos.forEachIndexed { indice, _ ->
                val colorPunto = when {
                    indice < indiceActivo -> Color(0xFF16A34A)
                    indice == indiceActivo -> Color(0xFF0891B2)
                    else -> Color(0xFFD1D5DB)
                }

                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(colorPunto, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (indice < indiceActivo) {
                        Text("✓", color = Color.White, fontSize = 13.sp)
                    } else {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color.White, CircleShape)
                        )
                    }
                }

                if (indice < pasos.lastIndex) {
                    val colorLinea = if (indice < indiceActivo) {
                        Color(0xFF16A34A)
                    } else {
                        Color(0xFFD1D5DB)
                    }

                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .height(3.dp)
                            .background(colorLinea)
                    )
                }
            }
        }

        Spacer(Modifier.height(5.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            pasos.forEachIndexed { indice, paso ->
                Text(
                    text = paso,
                    color = if (indice <= indiceActivo) {
                        Color(0xFF374151)
                    } else {
                        Color(0xFF9CA3AF)
                    },
                    fontSize = 10.sp
                )
            }
        }
    }
}