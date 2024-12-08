package com.matijasokol.notes.server.note.service

import arrow.core.Either
import com.matijasokol.notes.data.api.models.NoteDto
import com.matijasokol.notes.server.core.ServerError

interface NoteService {

    suspend fun create(
        noteDto: NoteDto,
    ): Either<ServerError, NoteDto>

    suspend fun getUserNotes(
        userId: String,
    ): Either<ServerError, List<NoteDto>>

    suspend fun getNote(
        noteId: String,
    ): Either<ServerError, NoteDto>
}
