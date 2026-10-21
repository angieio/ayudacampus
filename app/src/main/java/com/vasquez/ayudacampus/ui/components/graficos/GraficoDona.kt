package com.vasquez.ayudacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vasquez.ayudacampus.model.DatoGrafico

@Composable
fun GraficoDona(datos: List<DatoGrafico>) {
    val total = datos.sumOf { it.valor }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(145.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(Modifier.size(135.dp)) {
                val grosor = 19.dp.toPx()
                val margen = grosor / 2
                val diametro = size.minDimension - grosor
                var anguloInicial = -90f

                if (total > 0) {
                    datos.filter { it.valor > 0 }.forEach { dato ->
                        val barrido = dato.valor.toFloat() / total * 360f

                        drawArc(
                            color = Color(dato.color),
                            startAngle = anguloInicial,
                            sweepAngle = (barrido - 1f).coerceAtLeast(0f),
                            useCenter = false,
                            topLeft = Offset(margen, margen),
                            size = Size(diametro, diametro),
                            style = Stroke(
                                width = grosor,
                                cap = StrokeCap.Butt
                            )
                        )

                        anguloInicial += barrido
                    }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = total.toString(),
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("tickets", fontSize = 10.sp, color = Color.Gray)
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            datos.forEach { dato ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(13.dp)
                            .background(Color(dato.color), CircleShape)
                    )
                    Spacer(Modifier.width(7.dp))
                    Text(
                        text = dato.etiqueta,
                        modifier = Modifier.weight(1f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = dato.valor.toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}