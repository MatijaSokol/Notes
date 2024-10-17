package com.matijasokol.notes

import androidx.compose.ui.window.ComposeUIViewController
import com.matijasokol.notes.di.initKoin

fun MainViewController() = ComposeUIViewController(
  configure = {
    enforceStrictPlistSanityCheck = false
    initKoin()
  },
  content = { AppContent() },
)
