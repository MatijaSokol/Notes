package com.matijasokol.notes.auth

data class AuthState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val authType: AuthType = AuthType.Login,
)
