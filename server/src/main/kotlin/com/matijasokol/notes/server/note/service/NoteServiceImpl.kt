package com.matijasokol.notes.server.note.service

import arrow.core.Either
import arrow.core.raise.either
import com.matijasokol.notes.core.models.Email
import com.matijasokol.notes.core.models.Uuid
import com.matijasokol.notes.core.models.value
import com.matijasokol.notes.data.api.models.NoteDto
import com.matijasokol.notes.domain.UUIDProvider
import com.matijasokol.notes.server.NoteEntity
import com.matijasokol.notes.server.core.ServerError
import com.matijasokol.notes.server.core.errorAsIncorrectInput
import com.matijasokol.notes.server.database.mappers.toNoteDto
import com.matijasokol.notes.server.note.model.toNoteOrError
import com.matijasokol.notes.server.note.usecase.CreateNote
import com.matijasokol.notes.server.note.usecase.DeleteNote
import com.matijasokol.notes.server.note.usecase.EnsureUser
import com.matijasokol.notes.server.note.usecase.GetNoteById
import com.matijasokol.notes.server.note.usecase.GetUserIdByEmail
import com.matijasokol.notes.server.note.usecase.GetUserNotes
import com.matijasokol.notes.server.note.usecase.UpdateNote

@Suppress("LongParameterList")
class NoteServiceImpl(
    private val createNote: CreateNote,
    private val updateNote: UpdateNote,
    private val getUserNotes: GetUserNotes,
    private val getNoteById: GetNoteById,
    private val deleteNote: DeleteNote,
    private val getUserIdByEmail: GetUserIdByEmail,
    private val ensureUser: EnsureUser,
    private val uuidProvider: UUIDProvider,
) : NoteService {

    override suspend fun create(noteDto: NoteDto, userEmail: String): Either<ServerError, NoteDto> = either {
        val userId = getUserIdByEmail(
            email = Email(userEmail, "UserEmail").errorAsIncorrectInput().bind(),
        ).bind()

        val note = noteDto.toNoteOrError(
            id = uuidProvider.generateValue(),
            userId = userId.value,
        ).bind()

        createNote(
            id = note.id,
            title = note.title,
            text = note.text,
            userId = note.userId,
            createdAt = note.createdAt,
        ).map(NoteEntity::toNoteDto).bind()
    }

    override suspend fun update(noteDto: NoteDto, userEmail: String): Either<ServerError, NoteDto> = either {
        val note = noteDto.toNoteOrError().bind()

        ensureUser(
            userEmail = Email(userEmail, "UserEmail").errorAsIncorrectInput().bind(),
            noteId = note.id,
        ).bind()

        updateNote(
            id = note.id,
            title = note.title,
            text = note.text,
            userId = note.userId,
            createdAt = note.createdAt,
        ).map(NoteEntity::toNoteDto).bind()
    }

    override suspend fun delete(noteId: String, userEmail: String): Either<ServerError, Unit> = either {
        ensureUser(
            userEmail = Email(userEmail, "UserEmail").errorAsIncorrectInput().bind(),
            noteId = Uuid(noteId, "NoteId").errorAsIncorrectInput().bind(),
        ).bind()

        deleteNote(
            noteId = Uuid(noteId, "NoteId").errorAsIncorrectInput().bind(),
        ).bind()
    }

    override suspend fun getUserNotes(email: String): Either<ServerError, List<NoteDto>> = either {
        getUserNotes(
            email = Email(email, "UserEmail").errorAsIncorrectInput().bind(),
        ).bind().map(NoteEntity::toNoteDto)
    }

    override suspend fun getNote(noteId: String, userEmail: String): Either<ServerError, NoteDto> = either {
        ensureUser(
            userEmail = Email(userEmail, "UserEmail").errorAsIncorrectInput().bind(),
            noteId = Uuid(noteId, "NoteId").errorAsIncorrectInput().bind(),
        ).bind()

        getNoteById(
            noteId = Uuid(noteId, "NoteId").errorAsIncorrectInput().bind(),
        ).bind().toNoteDto()
    }
}
