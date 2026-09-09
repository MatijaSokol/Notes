package com.matijasokol.notes

import androidx.compose.ui.window.ComposeUIViewController
import com.matijasokol.notes.di.initKoin

@Suppress("FunctionName")
fun MainViewController() = ComposeUIViewController(
    configure = {
        enforceStrictPlistSanityCheck = false
        initKoin()
    },
    content = { AppContent(loggedIn = false) },
)
