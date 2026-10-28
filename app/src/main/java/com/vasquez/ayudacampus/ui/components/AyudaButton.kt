package com.vasquez.ayudacampus.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AyudaButton(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color? = null,
    contentColor: Color? = null,
    shape: Shape? = null,
    contenido: @Composable RowScope.() -> Unit = {
        Text(texto, fontWeight = FontWeight.SemiBold)
    }
) {
    val fondoBoton = containerColor ?: MaterialTheme.colorScheme.primary

    val textoBoton = contentColor ?: if (containerColor == null) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        Color.White
    }

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape ?: MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = fondoBoton,
            contentColor = textoBoton,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        contentPadding = ButtonDefaults.ContentPadding,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
        content = contenido
    )
}
