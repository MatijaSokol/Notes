package com.matijasokol.notes.server.note.usecase

import arrow.core.Either
import arrow.core.raise.either
import com.matijasokol.notes.core.models.Email
import com.matijasokol.notes.server.UserQueries
import com.matijasokol.notes.server.core.UserError
import com.matijasokol.notes.server.database.databaseOperation
import kotlin.uuid.Uuid
import kotlin.uuid.toKotlinUuid

class GetUserIdByEmail(private val userQueries: UserQueries) {

    suspend operator fun invoke(email: Email): Either<UserError, Uuid> = either {
        databaseOperation {
            userQueries.getByEmail(email.value).executeAsOneOrNull()?.id?.toKotlinUuid()
                ?: raise(UserError.UserNotFoundByEmail(email.value))
        }.bind()
    }
}
