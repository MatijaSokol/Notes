package com.matijasokol.notes.server.note.usecase

import arrow.core.Either
import arrow.core.raise.either
import com.matijasokol.notes.core.models.value
import com.matijasokol.notes.server.NoteEntity
import com.matijasokol.notes.server.NoteQueries
import com.matijasokol.notes.server.core.NoteError
import com.matijasokol.notes.server.database.databaseOperation
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

class GetNoteById(
    private val noteQueries: NoteQueries,
) {

    suspend operator fun invoke(noteId: Uuid): Either<NoteError, NoteEntity> = either {
        val noteOrNull = databaseOperation {
            noteQueries.getById(noteId.toJavaUuid()).executeAsOneOrNull()
        }.bind()

        noteOrNull ?: raise(NoteError.NoteNotFoundById(noteId.value))
    }
}
