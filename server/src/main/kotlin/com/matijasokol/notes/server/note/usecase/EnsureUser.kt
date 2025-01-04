package com.matijasokol.notes.server.note.usecase

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import com.matijasokol.notes.core.models.Email
import com.matijasokol.notes.core.models.InvalidField
import com.matijasokol.notes.core.models.value
import com.matijasokol.notes.server.core.ServerError
import com.matijasokol.notes.server.core.ValidationError
import com.matijasokol.notes.server.database.mappers.toNoteDto
import com.matijasokol.notes.server.note.model.Note
import com.matijasokol.notes.server.note.model.toNoteOrError
import kotlin.uuid.Uuid

class EnsureUser(
    private val getNoteById: GetNoteById,
    private val getUserIdByEmail: GetUserIdByEmail,
) {

    suspend operator fun invoke(
        userEmail: Email,
        noteId: Uuid,
    ): Either<ServerError, Note> = either {
        val note = getNoteById(
            noteId = noteId,
        ).bind().toNoteDto().toNoteOrError().bind()

        val userId = getUserIdByEmail(email = userEmail).bind()

        ensure(note.userId.value == userId.value) {
            ValidationError.IncorrectInput(
                InvalidField(
                    field = "UserId",
                    message = "User does not have permission to access this note",
                ),
            )
        }

        note
    }
}
