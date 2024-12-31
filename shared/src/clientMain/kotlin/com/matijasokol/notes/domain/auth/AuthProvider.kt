package com.matijasokol.notes.domain.auth

import arrow.core.Either
import com.matijasokol.notes.ClientError

interface AuthProvider {

    suspend fun getCurrentToken(): Either<ClientError, String>

    suspend fun userLoggedIn(): Either<ClientError, Boolean>

    suspend fun logout(): Either<ClientError, Unit>

    suspend fun loginWithGoogle(): Either<ClientError, AuthUser>

    suspend fun loginWithEmailAndPassword(email: String, password: String): Either<ClientError, AuthUser>

    suspend fun registerWithEmailAndPassword(email: String, password: String): Either<ClientError, AuthUser>
}
