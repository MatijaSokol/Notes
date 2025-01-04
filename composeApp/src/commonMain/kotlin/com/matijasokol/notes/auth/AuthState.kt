package com.matijasokol.notes.auth

data class AuthState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val authType: AuthType = AuthType.Login,
    val loginTopText: String = "",
    val loginBottomText: String = "",
    val registerTopText: String = "",
    val registerBottomText: String = "",
    val emailLabel: String = "",
    val passwordLabel: String = "",
    val loginLabel: String = "",
    val registerLabel: String = "",
    val typeSpacerText: String = "",
)
