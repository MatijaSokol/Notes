package com.matijasokol.notes.server.auth

import arrow.core.Either
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import com.matijasokol.notes.server.core.ValidationError

class VerifyToken(
    private val auth: FirebaseAuth,
) {
    operator fun invoke(token: String?): Either<ValidationError.InvalidToken, FirebaseToken> = Either.catch {
        auth.verifyIdToken(token)
    }.mapLeft {
        ValidationError.InvalidToken
    }
}
