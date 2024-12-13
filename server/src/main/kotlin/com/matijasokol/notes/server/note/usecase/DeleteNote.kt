package com.matijasokol.notes.server.note.usecase

import arrow.core.Either
import arrow.core.raise.either
import com.matijasokol.notes.core.models.value
import com.matijasokol.notes.server.NoteQueries
import com.matijasokol.notes.server.core.NoteError
import com.matijasokol.notes.server.database.databaseOperation
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

class DeleteNote(
    private val noteQueries: NoteQueries,
) {

    suspend operator fun invoke(
        noteId: Uuid,
    ): Either<NoteError, Unit> = either {
        val dbId = noteId.toJavaUuid()
        val deletedNoteId = databaseOperation { noteQueries.delete(dbId).executeAsOneOrNull() }.bind()

        when (deletedNoteId == dbId) {
            true -> Unit
            false -> raise(NoteError.NoteNotFoundById(noteId.value))
        }
    }
}
