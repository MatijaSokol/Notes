package com.matijasokol.notes.server.note.usecase

import arrow.core.Either
import arrow.core.raise.either
import com.matijasokol.notes.core.models.NonEmptyString
import com.matijasokol.notes.core.models.Timestamp
import com.matijasokol.notes.core.models.value
import com.matijasokol.notes.server.NoteEntity
import com.matijasokol.notes.server.NoteQueries
import com.matijasokol.notes.server.core.NoteError
import com.matijasokol.notes.server.database.databaseOperation
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

class UpdateNote(private val noteQueries: NoteQueries) {

    suspend operator fun invoke(
        id: Uuid,
        title: NonEmptyString,
        text: NonEmptyString,
        userId: Uuid,
        createdAt: Timestamp,
    ): Either<NoteError, NoteEntity> = either {
        val dbId = id.toJavaUuid()

        val updatedId = databaseOperation {
            noteQueries.update(
                title = title.value,
                text = text.value,
                id = dbId,
            ).executeAsOneOrNull()
        }.bind()

        when (updatedId == id.toJavaUuid()) {
            true -> NoteEntity(
                id = dbId,
                title = title.value,
                text = text.value,
                userId = userId.toJavaUuid(),
                createdAt = createdAt.value,
            )
            false -> raise(NoteError.NoteNotFoundById(id.value))
        }
    }
}
