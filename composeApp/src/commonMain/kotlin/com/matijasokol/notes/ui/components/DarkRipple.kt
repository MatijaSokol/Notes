package com.matijasokol.notes.ui.components

import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

@Composable
inline fun withDarkRipple(
    config: RippleConfiguration = RippleConfiguration(color = Color.Gray),
    crossinline content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalRippleConfiguration provides config) {
        content()
    }
}
