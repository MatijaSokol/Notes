package com.matijasokol.notes.domain.auth

import arrow.core.Either
import com.matijasokol.notes.ClientError
import com.matijasokol.notes.core.models.Email
import com.matijasokol.notes.core.models.NonEmptyString

interface AuthProvider {

    suspend fun getCurrentUserEmail(): Either<ClientError, String>

    suspend fun getCurrentToken(): Either<ClientError, String>

    suspend fun userLoggedIn(): Either<ClientError, Boolean>

    suspend fun logout(): Either<ClientError, Unit>

    suspend fun loginWithGoogle(): Either<ClientError, AuthUser>

    suspend fun loginWithEmailAndPassword(email: Email, password: NonEmptyString): Either<ClientError, AuthUser>

    suspend fun registerWithEmailAndPassword(email: Email, password: NonEmptyString): Either<ClientError, AuthUser>
}
