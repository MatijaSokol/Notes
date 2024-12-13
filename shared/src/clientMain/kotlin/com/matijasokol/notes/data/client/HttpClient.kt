package com.matijasokol.notes.data.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun buildHttpClient(
    engine: HttpClientEngineFactory<*>,
    json: Json,
): HttpClient = HttpClient(engine) {
    install(Resources)

    install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        url(BASE_URL)
    }

    install(HttpTimeout) { requestTimeoutMillis = TIMEOUT_MS }

    install(ContentNegotiation) {
        json(json)
    }
}

private const val BASE_URL = ""
private const val TIMEOUT_MS = 60_000L
