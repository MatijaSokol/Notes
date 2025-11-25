package com.matijasokol.notes

import arrow.core.Either
import com.matijasokol.notes.core.models.InvalidField

sealed class ClientError

sealed class ValidationError : ClientError() {
    data class IncorrectInput(val error: InvalidField) : ValidationError()
}

fun <T> Either<InvalidField, T>.errorAsIncorrectInput(): Either<ValidationError.IncorrectInput, T> =
    mapLeft(ValidationError::IncorrectInput)

sealed class NetworkError : ClientError() {
    data object UnknownNetworkError : NetworkError()

    data class BackendError(
        val responseCode: Int,
        val errorMessage: String?,
    ) : NetworkError()
}

sealed class AuthError : ClientError() {
    data object EmailNotAvailable : AuthError()

    data object TokenNotAvailable : AuthError()
}

sealed class RegistrationError : ClientError() {
    data object RegistrationFailed : RegistrationError()
}

sealed class LoginError : ClientError() {
    data object InvalidCredentials : LoginError()
}

sealed class DatabaseError : ClientError() {
    data object GenericError : DatabaseError()
}

sealed class NoteDatabaseError : DatabaseError() {
    data class NoteNotFound(val noteId: String) : NoteDatabaseError()
}
