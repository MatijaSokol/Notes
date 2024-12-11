package com.matijasokol.notes.domain.auth

import arrow.core.Either
import com.matijasokol.notes.ClientError

interface AuthProvider {

    suspend fun userLoggedIn(): Either<ClientError, Boolean>

    suspend fun loginWithGoogle(): Either<ClientError, AuthUser>

    suspend fun loginWithEmailAndPassword(email: String, password: String): Either<ClientError, AuthUser>
}
