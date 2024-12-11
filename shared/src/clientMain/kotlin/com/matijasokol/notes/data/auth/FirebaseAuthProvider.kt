package com.matijasokol.notes.data.auth

import arrow.core.Either
import arrow.core.raise.either
import com.matijasokol.notes.ClientError
import com.matijasokol.notes.LoginError
import com.matijasokol.notes.data.safeNetworkCall
import com.matijasokol.notes.domain.auth.AuthProvider
import com.matijasokol.notes.domain.auth.AuthUser
import dev.gitlive.firebase.auth.FirebaseAuth

class FirebaseAuthProvider(
    private val auth: FirebaseAuth,
) : AuthProvider {

    override suspend fun userLoggedIn(): Either<ClientError, Boolean> {
        return Either.Right(auth.currentUser != null)
    }

    override suspend fun loginWithGoogle(): Either<ClientError, AuthUser> {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String,
    ): Either<ClientError, AuthUser> = either {
        val result = safeNetworkCall {
            auth.signInWithEmailAndPassword(email, password).user
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
}
