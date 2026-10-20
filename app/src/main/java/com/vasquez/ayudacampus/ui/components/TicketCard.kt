package com.vasquez.ayudacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.VpnKey
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vasquez.ayudacampus.data.local.relation.TicketConDetalle

@Composable
fun TicketCard(
    detalle: TicketConDetalle,
    onClick: () -> Unit
) {
    val ticket = detalle.ticket
    val colorEstado = colorEstado(ticket.estado)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(112.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(112.dp)
                    .background(colorEstado)
            )

            Spacer(Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0xFFD9F8FA), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconoCategoria(detalle.categoria.icono),
                    contentDescription = null,
                    tint = Color(0xFF0799B4),
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = ticket.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF273244)
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = "#${ticket.id.toString().padStart(4, '0')} · ${ticket.ubicacion}",
                    fontSize = 11.sp,
                    color = Color(0xFF8993A1),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(14.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    EtiquetaTicket(
                        texto = estadoVisible(ticket.estado),
                        fondo = fondoEstado(ticket.estado),
                        textoColor = textoEstado(ticket.estado)
                    )

                    Spacer(Modifier.width(6.dp))

                    EtiquetaTicket(
                        texto = ticket.prioridad.lowercase()
                            .replaceFirstChar { it.uppercase() },
                        fondo = fondoPrioridad(ticket.prioridad),
                        textoColor = textoPrioridad(ticket.prioridad)
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = tiempoRelativo(ticket.fecha),
                        fontSize = 10.sp,
                        color = Color(0xFF929AA7)
                    )
                }
            }
        }
    }
}

@Composable
private fun EtiquetaTicket(
    texto: String,
    fondo: Color,
    textoColor: Color
) {
    Surface(
        color = fondo,
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = texto,
            color = textoColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

private fun iconoCategoria(nombreIcono: String): ImageVector =
    when (nombreIcono.lowercase()) {
        "wifi" -> Icons.Outlined.Wifi
        "pc" -> Icons.Outlined.Computer
        "printer" -> Icons.Outlined.Print
        "key" -> Icons.Outlined.VpnKey
        else -> Icons.Outlined.Code
    }

private fun colorEstado(estado: String): Color =
    when (estado) {
        "ABIERTO" -> Color(0xFFD97706)
        "EN_PROCESO" -> Color(0xFFEF4444)
        "RESUELTO" -> Color(0xFF7C8795)
        "CERRADO" -> Color(0xFF7C8795)
        else -> Color.Gray
    }

private fun estadoVisible(estado: String): String =
    when (estado) {
        "ABIERTO" -> "Abierto"
        "EN_PROCESO" -> "En proceso"
        "RESUELTO" -> "Resuelto"
        "CERRADO" -> "Cerrado"
        else -> estado
    }

private fun fondoEstado(estado: String): Color =
    when (estado) {
        "ABIERTO" -> Color(0xFFFFF4C9)
        "EN_PROCESO" -> Color(0xFFE8F2FF)
        "RESUELTO" -> Color(0xFFDCFCE8)
        "CERRADO" -> Color(0xFFF0F2F5)
        else -> Color.LightGray
    }

private fun textoEstado(estado: String): Color =
    when (estado) {
        "ABIERTO" -> Color(0xFFAE7A00)
        "EN_PROCESO" -> Color(0xFF2772C7)
        "RESUELTO" -> Color(0xFF16864C)
        "CERRADO" -> Color(0xFF667080)
        else -> Color.DarkGray
    }

private fun fondoPrioridad(prioridad: String): Color =
    when (prioridad) {
        "ALTA" -> Color(0xFFFFEDED)
        "MEDIA" -> Color(0xFFFFF0DB)
        else -> Color(0xFFF0F2F5)
    }

private fun textoPrioridad(prioridad: String): Color =
    when (prioridad) {
        "ALTA" -> Color(0xFFE34B4B)
        "MEDIA" -> Color(0xFFB46A16)
        else -> Color(0xFF667080)
    }

private fun tiempoRelativo(fecha: Long): String {
    val diferencia = (System.currentTimeMillis() - fecha).coerceAtLeast(0)
    val hora = 60 * 60 * 1000L
    val dia = 24 * hora

    return when {
        diferencia < hora -> "Hace ${maxOf(1, diferencia / 60_000)} min"
        diferencia < dia -> "Hace ${diferencia / hora} h"
        diferencia < 2 * dia -> "Ayer"
        else -> "Hace ${diferencia / dia} d"
    }
}