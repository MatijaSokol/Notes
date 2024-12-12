package com.matijasokol.notes.data.client

import kotlinx.serialization.json.Json

val json = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
}
