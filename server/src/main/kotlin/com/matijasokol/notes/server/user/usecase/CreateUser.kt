package com.matijasokol.notes.server.user.usecase

import arrow.core.Either
import com.matijasokol.notes.core.models.Email
import com.matijasokol.notes.domain.UUIDProvider
import com.matijasokol.notes.server.UserEntity
import com.matijasokol.notes.server.UserQueries
import com.matijasokol.notes.server.core.DatabaseError
import com.matijasokol.notes.server.database.databaseOperation
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

class CreateUser(
    private val userQueries: UserQueries,
    private val uuidProvider: UUIDProvider,
) {

    suspend operator fun invoke(
        email: Email,
        id: Uuid = uuidProvider.generate(),
    ): Either<DatabaseError, UserEntity> {
        val dbId = id.toJavaUuid()

        return databaseOperation {
            userQueries.insert(
                id = dbId,
                email = email.value,
            )
        }.map { UserEntity(dbId, email.value) }
    }
}
