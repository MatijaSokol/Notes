package com.matijasokol.notes.server.note.usecase

import arrow.core.Either
import arrow.core.raise.either
import com.matijasokol.notes.core.models.Email
import com.matijasokol.notes.server.NoteEntity
import com.matijasokol.notes.server.NoteQueries
import com.matijasokol.notes.server.core.UserError
import com.matijasokol.notes.server.database.databaseOperation
import kotlin.uuid.toJavaUuid

class GetUserNotes(
    private val noteQueries: NoteQueries,
    private val getUserIdByEmail: GetUserIdByEmail,
) {

    suspend operator fun invoke(email: Email): Either<UserError, List<NoteEntity>> = either {
        val userId = getUserIdByEmail(email).bind()

        databaseOperation {
            noteQueries.getAllByUserId(userId.toJavaUuid()).executeAsList()
        }.bind()
    }
}
