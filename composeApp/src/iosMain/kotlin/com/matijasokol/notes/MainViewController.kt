package com.matijasokol.notes

import androidx.compose.ui.window.ComposeUIViewController
import com.matijasokol.notes.di.coreModule
import com.matijasokol.notes.di.initKoin

fun MainViewController() = ComposeUIViewController(
  configure = { initKoin { modules(coreModule) } },
) { AppContent() }
