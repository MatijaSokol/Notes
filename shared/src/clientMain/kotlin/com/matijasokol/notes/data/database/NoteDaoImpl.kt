package com.matijasokol.notes.data.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import arrow.core.Either
import arrow.core.raise.either
import arrow.core.right
import com.matijasokol.notes.AppDispatchers
import com.matijasokol.notes.DatabaseError
import com.matijasokol.notes.NoteDatabaseError
import com.matijasokol.notes.client.NoteEntity
import com.matijasokol.notes.client.NoteQueries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteDaoImpl(
    private val noteQueries: NoteQueries,
    private val appDispatchers: AppDispatchers,
) : NoteDao {

    override suspend fun upsertNote(note: NoteEntity): Either<DatabaseError, Unit> {
        val updateResult = updateNote(note)

        (updateResult as? Either.Right)
            ?.value
            ?.takeIf { it > 0L }
            ?.let { return Unit.right() }

        return insertNote(note)
    }

    private suspend fun insertNote(note: NoteEntity): Either<DatabaseError, Unit> =
        databaseOperation(appDispatchers) {
            noteQueries.insert(
                id = note.id,
                title = note.title,
                text = note.text,
                userId = note.userId,
                created_at = note.created_at,
                waiting_for_upload = note.waiting_for_upload,
                waiting_for_delete = note.waiting_for_delete,
            )
        }

    private suspend fun updateNote(note: NoteEntity): Either<DatabaseError, Long?> =
        databaseOperation(appDispatchers) {
            noteQueries.update(
                id = note.id,
                title = note.title,
                text = note.text,
                created_at = note.created_at,
                waiting_for_upload = note.waiting_for_upload,
                waiting_for_delete = note.waiting_for_delete,
            )

            noteQueries.changes().executeAsOneOrNull()
        }

    override suspend fun deleteNoteById(noteId: String): Either<DatabaseError, Unit> =
        databaseOperation(appDispatchers) { noteQueries.delete(noteId) }

    override suspend fun getNoteById(noteId: String): Either<DatabaseError, NoteEntity> = either {
        databaseOperation(appDispatchers) {
            noteQueries.getById(noteId).executeAsOneOrNull()
                ?: raise(NoteDatabaseError.NoteNotFound(noteId))
        }.bind()
    }

    override fun observeAllNotes(): Flow<List<NoteEntity>> = noteQueries.getAll()
        .asFlow()
        .mapToList(appDispatchers.io)

    override fun observeUnsyncedNotes(): Flow<List<NoteEntity>> = noteQueries.getUnsyncedData()
        .asFlow()
        .mapToList(appDispatchers.io)

    override fun unsyncedDataExists(): Flow<Boolean> = observeUnsyncedNotes()
        .map(List<NoteEntity>::isNotEmpty)

    override suspend fun deleteAllNotes() {
        databaseOperation(appDispatchers) { noteQueries.deleteAll() }
    }
}
