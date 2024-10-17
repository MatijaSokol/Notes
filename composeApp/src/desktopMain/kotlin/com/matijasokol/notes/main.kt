@file:Suppress("ktlint:standard:filename")

package com.matijasokol.notes

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.matijasokol.notes.di.initKoin

fun main() {
    initKoin()

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Notes",
        ) {
            AppContent()
        }
    }
}
