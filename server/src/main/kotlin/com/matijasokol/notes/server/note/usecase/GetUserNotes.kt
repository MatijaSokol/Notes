package com.matijasokol.notes.server.note.usecase

import arrow.core.Either
import com.matijasokol.notes.core.models.Email
import com.matijasokol.notes.server.NoteEntity
import com.matijasokol.notes.server.NoteQueries
import com.matijasokol.notes.server.UserQueries
import com.matijasokol.notes.server.core.DatabaseError
import com.matijasokol.notes.server.database.databaseOperation

class GetUserNotes(
    private val noteQueries: NoteQueries,
    private val userQueries: UserQueries,
) {

    suspend operator fun invoke(email: Email): Either<DatabaseError, List<NoteEntity>> = databaseOperation {
        val userId = userQueries.getByEmail(email.value).executeAsOne().id
        noteQueries.getAllByUserId(userId).executeAsList()
    }
}
