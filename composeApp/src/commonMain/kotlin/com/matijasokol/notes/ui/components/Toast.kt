package com.matijasokol.notes.ui.components

interface Toast {
    fun show(message: String, duration: ToastDuration = ToastDuration.SHORT)
}

enum class ToastDuration {
    SHORT, LONG
}
