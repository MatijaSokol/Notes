package com.matijasokol.notes.domain.notes

import arrow.core.Either
import com.matijasokol.notes.ClientError
import com.matijasokol.notes.DatabaseError
import com.matijasokol.notes.NetworkError
import com.matijasokol.notes.domain.notes.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {

    suspend fun create(note: Note): Either<ClientError, Note>

    suspend fun update(note: Note): Either<ClientError, Note>

    suspend fun delete(noteId: String): Either<ClientError, Unit>

    suspend fun getNoteById(noteId: String): Either<DatabaseError, Note>

    suspend fun getCurrentUserNotes(): Either<NetworkError, Unit>

    fun observeLocalUserNotes(): Flow<List<Note>>

    fun unsyncedDataExists(): Flow<Boolean>

    suspend fun syncNotes()
}
