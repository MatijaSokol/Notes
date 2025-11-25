package com.matijasokol.notes.server.core

import arrow.core.Either
import arrow.core.NonEmptyList
import arrow.core.nonEmptyListOf
import com.matijasokol.notes.core.models.InvalidField

fun <T> Either<InvalidField, T>.errorAsIncorrectInput(): Either<ValidationError.IncorrectInput, T> =
    mapLeft(ValidationError::IncorrectInput)

sealed interface ServerError

sealed interface ValidationError : ServerError {
    data object InvalidToken : ValidationError

    data class IncorrectInput(val errors: NonEmptyList<InvalidField>) : ValidationError {
        constructor(head: InvalidField) : this(nonEmptyListOf(head))
    }
}

sealed interface UserError : ServerError {
    data class UserNotFoundById(val id: String) : UserError

    data class UserNotFoundByEmail(val email: String) : UserError
}

sealed interface NoteError : ServerError {
    data class NoteNotFoundById(val id: String) : NoteError
}

sealed interface DatabaseError :
    UserError,
    ValidationError,
    NoteError {
    data class RecordAlreadyExists(val message: String?) : DatabaseError

    data class ForeignKeyViolation(val message: String?) : DatabaseError
}
