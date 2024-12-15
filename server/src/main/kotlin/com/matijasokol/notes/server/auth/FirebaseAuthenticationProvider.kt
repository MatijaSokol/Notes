package com.matijasokol.notes.server.auth

import arrow.core.Either.Left
import arrow.core.Either.Right
import com.matijasokol.notes.data.api.TOKEN_HEADER
import com.matijasokol.notes.server.auth.FirebaseAuthenticationProvider.Configuration
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.AuthenticationContext
import io.ktor.server.auth.AuthenticationFailedCause
import io.ktor.server.auth.AuthenticationProvider
import io.ktor.server.response.respond

private class FirebaseAuthenticationProvider(
    config: Config,
    private val verifyToken: VerifyToken,
) : AuthenticationProvider(config) {

    class Configuration(name: String?) : Config(name)

    override suspend fun onAuthenticate(context: AuthenticationContext) {
        val call = context.call
        val token = call.request.headers[TOKEN_HEADER]

        when (val result = verifyToken(token)) {
            is Left -> context.challenge(
                key = FIREBASE_AUTH,
                cause = AuthenticationFailedCause.InvalidCredentials,
            ) { challenge, _ ->
                call.respond(status = HttpStatusCode.Unauthorized, message = MESSAGE_UNAUTHORIZED)
                challenge.complete()
            }

            is Right -> context.principal(TokenPrincipal(result.value))
        }
    }
}

fun AuthenticationConfig.firebase(verifyToken: VerifyToken, name: String? = null) {
    register(
        provider = FirebaseAuthenticationProvider(
            config = Configuration(name),
            verifyToken = verifyToken,
        ),
    )
}

private const val FIREBASE_AUTH = "FirebaseAuth"
private const val MESSAGE_UNAUTHORIZED = "Unauthorized access"
