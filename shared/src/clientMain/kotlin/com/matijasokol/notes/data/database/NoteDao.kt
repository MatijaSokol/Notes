package com.matijasokol.notes.data.database

import arrow.core.Either
import com.matijasokol.notes.DatabaseError
import com.matijasokol.notes.client.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteDao {

    suspend fun upsertNote(note: NoteEntity): Either<DatabaseError, Unit>

    suspend fun deleteNoteById(noteId: String): Either<DatabaseError, Unit>

    suspend fun getNoteById(noteId: String): Either<DatabaseError, NoteEntity>

    fun observeAllNotes(): Flow<List<NoteEntity>>

    fun unsyncedDataExists(): Flow<Boolean>
}
