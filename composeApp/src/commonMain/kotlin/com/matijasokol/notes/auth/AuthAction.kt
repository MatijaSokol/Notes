package com.matijasokol.notes.auth

sealed interface AuthAction {
    data object LoginSuccess : AuthAction
    data object LoginError : AuthAction
}
