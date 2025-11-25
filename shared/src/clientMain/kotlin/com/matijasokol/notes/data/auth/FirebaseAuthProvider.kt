package com.matijasokol.notes.data.auth

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import com.matijasokol.notes.AuthError
import com.matijasokol.notes.ClientError
import com.matijasokol.notes.LoginError
import com.matijasokol.notes.RegistrationError
import com.matijasokol.notes.core.models.Email
import com.matijasokol.notes.core.models.NonEmptyString
import com.matijasokol.notes.data.safeNetworkCall
import com.matijasokol.notes.domain.auth.AuthProvider
import com.matijasokol.notes.domain.auth.AuthUser
import dev.gitlive.firebase.auth.FirebaseAuth

class FirebaseAuthProvider(private val auth: FirebaseAuth) : AuthProvider {

    override suspend fun getCurrentUserEmail(): Either<ClientError, String> = either {
        Either.catch {
            ensureNotNull(auth.currentUser?.email) { AuthError.EmailNotAvailable }
        }.mapLeft {
            AuthError.EmailNotAvailable
        }.bind()
    }

    override suspend fun getCurrentToken(): Either<AuthError, String> = either {
        Either.catch {
            val token = auth.currentUser?.getIdToken(true)
            ensureNotNull(token) { AuthError.TokenNotAvailable }
        }.mapLeft {
            AuthError.TokenNotAvailable
        }.bind()
    }

    override suspend fun userLoggedIn(): Either<ClientError, Boolean> =
        Either.Right(auth.currentUser != null)

    override suspend fun logout(): Either<ClientError, Unit> = safeNetworkCall {
        auth.signOut()
    }

    override suspend fun loginWithGoogle(): Either<ClientError, AuthUser> {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithEmailAndPassword(
        email: Email,
        password: NonEmptyString,
    ): Either<ClientError, AuthUser> = either {
        val result = safeNetworkCall {
            auth.signInWithEmailAndPassword(email.value, password.value).user
        }.bind()

        return when (result == null) {
            true -> Either.Left(LoginError.InvalidCredentials)
            false -> Either.Right(
                value = AuthUser(
                    uid = result.uid,
                    displayName = result.displayName,
                    email = result.email,
                    photoURL = result.photoURL,
                ),
            )
        }
    }

    override suspend fun registerWithEmailAndPassword(
        email: Email,
        password: NonEmptyString,
    ): Either<ClientError, AuthUser> = either {
        val result = safeNetworkCall {
            auth.createUserWithEmailAndPassword(email.value, password.value).user
        }.bind()

        return when (result == null) {
            true -> Either.Left(RegistrationError.RegistrationFailed)
            false -> Either.Right(
                value = AuthUser(
                    uid = result.uid,
                    displayName = result.displayName,
                    email = result.email,
                    photoURL = result.photoURL,
                ),
            )
        }
    }
}
