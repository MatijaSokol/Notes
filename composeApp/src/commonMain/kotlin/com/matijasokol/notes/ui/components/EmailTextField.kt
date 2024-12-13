package com.matijasokol.notes.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun EmailTextField(
    value: String,
    modifier: Modifier = Modifier,
    labelValue: String = "",
    icon: ImageVector = Icons.Default.Email,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = labelValue) },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "Email",
            )
        },
        keyboardOptions = keyboardOptions,
    )
}
