package com.matijasokol.notes.data.client

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin

actual fun httpClientEngineFactory(): HttpClientEngineFactory<*> = Darwin
