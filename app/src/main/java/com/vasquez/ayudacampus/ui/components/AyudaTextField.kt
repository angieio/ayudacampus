package com.vasquez.ayudacampus.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun AyudaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    esClave: Boolean = false,
    modifier: Modifier = Modifier,
    icono: ImageVector? = null,
    color: Color = Color(0xFF0891B2)
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = modifier,
        leadingIcon = if (icono != null) {
            { Icon(imageVector = icono, contentDescription = null, tint = color) }
        } else {
            null
        },
        visualTransformation = if (esClave) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (esClave) KeyboardType.Password else KeyboardType.Email
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = color,
            focusedLabelColor = color,
            cursorColor = color
        )
    )
}