package com.matijasokol.notes.data.client

import com.matijasokol.notes.data.api.TOKEN_HEADER
import com.matijasokol.notes.domain.auth.AuthProvider
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header

fun HttpClient.withTokenInterceptor(
    authProvider: AuthProvider,
) = apply {
    plugin(HttpSend).intercept { request ->
        authProvider.getCurrentToken().getOrNull()?.let { token ->
            request.header(TOKEN_HEADER, token)
        }
        execute(request)
    }
}
