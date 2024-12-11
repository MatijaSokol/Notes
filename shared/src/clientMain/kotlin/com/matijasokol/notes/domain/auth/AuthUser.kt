package com.matijasokol.notes.domain.auth

data class AuthUser(
    val uid: String,
    val displayName: String?,
    val email: String?,
    val photoURL: String?,
)
