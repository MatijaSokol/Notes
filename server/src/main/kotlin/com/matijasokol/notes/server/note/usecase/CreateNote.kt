package com.matijasokol.notes.server.note.usecase

import arrow.core.Either
import com.matijasokol.notes.core.models.NonEmptyString
import com.matijasokol.notes.core.models.Timestamp
import com.matijasokol.notes.domain.UUIDProvider
import com.matijasokol.notes.server.NoteEntity
import com.matijasokol.notes.server.NoteQueries
import com.matijasokol.notes.server.core.DatabaseError
import com.matijasokol.notes.server.database.databaseOperation
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

class CreateNote(
    private val noteQueries: NoteQueries,
    private val uuidProvider: UUIDProvider,
) {

    suspend operator fun invoke(
        text: NonEmptyString,
        userId: Uuid,
        createdAt: Timestamp,
        id: Uuid = uuidProvider.generate(),
    ): Either<DatabaseError, NoteEntity> {
        val dbId = id.toJavaUuid()
        val userDbId = userId.toJavaUuid()

        return databaseOperation {
            noteQueries.insert(
                id = dbId,
                text = text.value,
                userId = userDbId,
                createdAt = createdAt.value,
            )
        }.map { NoteEntity(dbId, text.value, userDbId, createdAt.value) }
    }
}
