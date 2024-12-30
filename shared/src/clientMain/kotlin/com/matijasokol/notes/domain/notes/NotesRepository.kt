package com.matijasokol.notes.domain.notes

import arrow.core.Either
import com.matijasokol.notes.NetworkError
import com.matijasokol.notes.data.api.models.NoteDto

interface NotesRepository {

    suspend fun create(note: NoteDto): Either<NetworkError, NoteDto>

    suspend fun delete(noteId: String): Either<NetworkError, Unit>

    suspend fun getNoteById(noteId: String): Either<NetworkError, NoteDto>

    suspend fun getCurrentUserNotes(): Either<NetworkError, List<NoteDto>>
}
