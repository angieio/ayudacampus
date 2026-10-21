package com.vasquez.ayudacampus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vasquez.ayudacampus.data.local.entity.Comentario
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BurbujaComentario(
    comentario: Comentario,
    esPropio: Boolean,
    colorPropio: Color = Color(0xFF0891B2)
) {
    val colorBurbuja = if (esPropio) {
        colorPropio
    } else {
        Color(0xFFEDE4FF)
    }

    val colorTexto = if (esPropio) {
        Color.White
    } else {
        Color(0xFF252238)
    }

    val hora = SimpleDateFormat("HH:mm", Locale.getDefault())
        .format(Date(comentario.fecha))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = if (esPropio) {
            Arrangement.End
        } else {
            Arrangement.Start
        }
    ) {
        Surface(
            color = colorBurbuja,
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth(0.78f)
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = 14.dp,
                    vertical = 10.dp
                ),
                horizontalAlignment = if (esPropio) {
                    Alignment.End
                } else {
                    Alignment.Start
                }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (esPropio) "Tú" else comentario.autorNombre,
                        color = colorTexto,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = hora,
                        color = colorTexto.copy(alpha = 0.75f),
                        fontSize = 10.sp
                    )
                }

                Text(
                    text = comentario.texto,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    color = colorTexto,
                    fontSize = 13.sp
                )
            }
        }
    }
}
