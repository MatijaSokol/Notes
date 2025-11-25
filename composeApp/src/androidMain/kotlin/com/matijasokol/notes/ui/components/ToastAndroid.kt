package com.matijasokol.notes.ui.components

import android.content.Context
import com.matijasokol.notes.ui.components.ToastDuration.LONG
import com.matijasokol.notes.ui.components.ToastDuration.SHORT

class ToastAndroid(private val context: Context) : Toast {

    override fun show(message: String, duration: ToastDuration) {
        android.widget.Toast.makeText(
            context,
            message,
            when (duration) {
                SHORT -> android.widget.Toast.LENGTH_SHORT
                LONG -> android.widget.Toast.LENGTH_LONG
            },
        ).show()
    }
}
