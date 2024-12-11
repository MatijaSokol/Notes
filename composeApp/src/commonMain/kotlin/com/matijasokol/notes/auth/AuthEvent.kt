package com.matijasokol.notes.auth

sealed interface AuthEvent {
    data class EmailChanged(val email: String) : AuthEvent
    data class PasswordChanged(val password: String) : AuthEvent
    data class LoginClicked(val email: String, val password: String) : AuthEvent
    data object GoogleSignInClicked : AuthEvent
}
