package com.matijasokol.notes.server.plugins

import com.matijasokol.notes.server.auth.VerifyToken
import com.matijasokol.notes.server.auth.firebase
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import org.koin.ktor.ext.get

fun Application.configureAuth(
    verifyToken: VerifyToken = get(),
) {
    install(Authentication) {
        firebase(verifyToken)
    }
}
