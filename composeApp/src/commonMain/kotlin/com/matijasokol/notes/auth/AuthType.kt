package com.matijasokol.notes.auth

enum class AuthType {
    Login,
    Registration,
    ;

    fun toggle() = when (this) {
        Login -> Registration
        Registration -> Login
    }
}
