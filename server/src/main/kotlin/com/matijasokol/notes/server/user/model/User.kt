package com.matijasokol.notes.server.user.model

import arrow.core.Either
import com.matijasokol.notes.core.models.Email
import com.matijasokol.notes.core.models.Uuid
import com.matijasokol.notes.data.api.models.UserDto
import com.matijasokol.notes.server.core.ValidationError
import kotlin.uuid.Uuid

data class User(
    val id: Uuid,
    val email: Email,
) {

    companion object {
        operator fun invoke(
            id: String,
            email: String,
        ): Either<ValidationError.IncorrectInput, User> = Either.zipOrAccumulate(
            Uuid(value = id, field = "Id"),
            Email(value = email, field = "Email"),
            ::User,
        ).mapLeft(ValidationError::IncorrectInput)
    }
}

fun UserDto.toUserOrError(): Either<ValidationError.IncorrectInput, User> = User(id, email)
