package com.matijasokol.notes.auth

sealed interface AuthEvent {
    data class EmailChanged(val authType: AuthType, val email: String) : AuthEvent
    data class PasswordChanged(val authType: AuthType, val password: String) : AuthEvent
    data object TogglePasswordVisibility : AuthEvent
    data class LoginClicked(val email: String, val password: String) : AuthEvent
    data class RegistrationClicked(val email: String, val password: String) : AuthEvent
    data object GoogleSignInClicked : AuthEvent
    data object ToggleAuthType : AuthEvent
}
