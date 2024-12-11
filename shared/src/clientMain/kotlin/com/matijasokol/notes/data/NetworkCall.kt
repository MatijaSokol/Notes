package com.matijasokol.notes.data

import arrow.core.Either
import com.matijasokol.notes.NetworkError
import io.ktor.client.plugins.ResponseException

suspend fun <T> safeNetworkCall(
    apiCall: suspend () -> T,
): Either<NetworkError, T> = Either.catch {
    apiCall()
}.mapLeft { error ->
    if (error is ResponseException) {
        NetworkError.BackendError(
            responseCode = error.response.status.value,
            errorMessage = error.message,
        )
    } else {
        NetworkError.UnknownNetworkError
    }
}
