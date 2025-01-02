package com.matijasokol.notes.server.note.service

import arrow.core.Either
import arrow.core.raise.either
import com.matijasokol.notes.core.models.Email
import com.matijasokol.notes.core.models.Uuid
import com.matijasokol.notes.data.api.models.NoteDto
import com.matijasokol.notes.server.NoteEntity
import com.matijasokol.notes.server.core.ServerError
import com.matijasokol.notes.server.core.errorAsIncorrectInput
import com.matijasokol.notes.server.database.mappers.toNoteDto
import com.matijasokol.notes.server.note.model.toNoteOrError
import com.matijasokol.notes.server.note.usecase.CreateNote
import com.matijasokol.notes.server.note.usecase.DeleteNote
import com.matijasokol.notes.server.note.usecase.GetNoteById
import com.matijasokol.notes.server.note.usecase.GetUserNotes

class NoteServiceImpl(
    private val createNote: CreateNote,
    private val getUserNotes: GetUserNotes,
    private val getNoteById: GetNoteById,
    private val deleteNote: DeleteNote,
) : NoteService {

    override suspend fun create(noteDto: NoteDto): Either<ServerError, NoteDto> = either {
        val note = noteDto.toNoteOrError().bind()

        createNote(
            id = note.id,
            title = note.title,
            text = note.text,
            userId = note.userId,
            createdAt = note.createdAt,
        ).map(NoteEntity::toNoteDto).bind()
    }

    override suspend fun delete(noteId: String): Either<ServerError, Unit> = either {
        deleteNote(
            noteId = Uuid(noteId, "NoteId").errorAsIncorrectInput().bind(),
        ).bind()
    }

    override suspend fun getUserNotes(email: String): Either<ServerError, List<NoteDto>> = either {
        getUserNotes(
            email = Email(email, "UserEmail").errorAsIncorrectInput().bind(),
        ).bind().map(NoteEntity::toNoteDto)
    }

    override suspend fun getNote(noteId: String): Either<ServerError, NoteDto> = either {
        getNoteById(
            noteId = Uuid(noteId, "NoteId").errorAsIncorrectInput().bind(),
        ).bind().toNoteDto()
    }
}
