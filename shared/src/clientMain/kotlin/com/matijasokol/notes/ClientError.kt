package com.matijasokol.notes

sealed class ClientError

sealed class NetworkError : ClientError() {
    data object UnknownNetworkError : NetworkError()
    data class BackendError(val responseCode: Int, val errorMessage: String?) : NetworkError()
}

sealed class AuthError : ClientError() {
    data object TokenNotAvailable : AuthError()
}

sealed class RegistrationError : ClientError() {
    data object RegistrationFailed : RegistrationError()
}

sealed class LoginError : ClientError() {
    data object InvalidCredentials : LoginError()
}
