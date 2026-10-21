package com.vasquez.ayudacampus.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Estrellas(
    seleccion: Int,
    onSeleccionar: (Int) -> Unit,
    habilitadas: Boolean = true
) {
    Row(
        horizontalArrangement = Arrangement.Center
    ) {
        for (numero in 1..5) {
            Text(
                text = if (numero <= seleccion) "★" else "☆",
                color = if (numero <= seleccion) {
                    Color(0xFFF59E0B)
                } else {
                    Color(0xFFD1D5DB)
                },
                fontSize = 34.sp,
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .semantics {
                        contentDescription = "$numero estrellas"
                    }
                    .then(
                        if (habilitadas) {
                            Modifier.clickable {
                                onSeleccionar(numero)
                            }
                        } else {
                            Modifier
                        }
                    )
            )
        }
    }
}