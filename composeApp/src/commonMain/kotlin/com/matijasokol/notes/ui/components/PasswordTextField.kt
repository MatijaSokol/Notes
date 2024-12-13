package com.matijasokol.notes.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun PasswordTextField(
    value: String,
    modifier: Modifier = Modifier,
    valueVisible: Boolean = false,
    labelValue: String = "",
    icon: ImageVector = Icons.Default.Lock,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    visualTransformation: VisualTransformation = when (valueVisible) {
        true -> VisualTransformation.None
        false -> PasswordVisualTransformation()
    },
    trailingIcon: ImageVector = when (valueVisible) {
        true -> Icons.Filled.Visibility
        false -> Icons.Outlined.VisibilityOff
    },
    onValueChange: (String) -> Unit,
    onValueVisibleToggle: () -> Unit,
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
                contentDescription = "Password",
            )
        },
        trailingIcon = {
            IconButton(onClick = onValueVisibleToggle) {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = if (valueVisible) "Show Password" else "Hide Password",
                )
            }
        },
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
    )
}
