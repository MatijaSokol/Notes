package com.matijasokol.notes.server.note.usecase

import arrow.core.Either
import com.matijasokol.notes.server.NoteEntity
import com.matijasokol.notes.server.NoteQueries
import com.matijasokol.notes.server.core.DatabaseError
import com.matijasokol.notes.server.database.databaseOperation
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid

class GetUserNotes(
    private val noteQueries: NoteQueries,
) {

    suspend operator fun invoke(userId: Uuid): Either<DatabaseError, List<NoteEntity>> = databaseOperation {
        noteQueries.getAllByUserId(userId.toJavaUuid()).executeAsList()
    }
}
