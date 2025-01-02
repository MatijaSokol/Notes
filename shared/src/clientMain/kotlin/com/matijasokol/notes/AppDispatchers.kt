package com.matijasokol.notes

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlin.coroutines.CoroutineContext

data class AppDispatchers(
    val main: CoroutineContext = Dispatchers.Main,
    val io: CoroutineContext = Dispatchers.IO,
    val default: CoroutineContext = Dispatchers.Default,
)
