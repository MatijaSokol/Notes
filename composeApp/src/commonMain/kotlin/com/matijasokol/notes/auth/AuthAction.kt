package com.matijasokol.notes.auth

sealed interface AuthAction {
    data object LoginSuccess : AuthAction

    data class LoginError(val message: String) : AuthAction

    data object RegistrationSuccess : AuthAction

    data class RegistrationError(val message: String) : AuthAction
}
