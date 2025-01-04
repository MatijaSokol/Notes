package com.matijasokol.notes.data.notes

import arrow.core.Either
import arrow.core.raise.either
import com.matijasokol.notes.ClientError
import com.matijasokol.notes.DatabaseError
import com.matijasokol.notes.NetworkError
import com.matijasokol.notes.client.NoteEntity
import com.matijasokol.notes.data.api.V1
import com.matijasokol.notes.data.api.models.NoteDto
import com.matijasokol.notes.data.database.NoteDao
import com.matijasokol.notes.data.safeNetworkCall
import com.matijasokol.notes.domain.notes.NotesRepository
import com.matijasokol.notes.domain.notes.model.Note
import com.matijasokol.notes.domain.notes.model.toDto
import com.matijasokol.notes.domain.notes.model.toEntity
import com.matijasokol.notes.domain.notes.model.toNote
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.delete
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.plugins.resources.put
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class NotesRepositoryImpl(
    private val httpClient: HttpClient,
    private val noteDao: NoteDao,
) : NotesRepository {

    override suspend fun create(note: Note): Either<ClientError, Note> = either {
        noteDao.upsertNote(note.toEntity(waitingForUpload = true)).bind()

        val networkResult = safeNetworkCall {
            httpClient.post(V1.CreateNote()) { setBody(note.toDto()) }.body<NoteDto>().toNote()
        }

        when (networkResult) {
            is Either.Left -> note
            is Either.Right -> networkResult.value.also { networkNote ->
                noteDao.deleteNoteById(note.id).bind()
                noteDao.upsertNote(networkNote.toEntity(waitingForUpload = false)).bind()
            }
        }
    }

    override suspend fun update(note: Note): Either<ClientError, Note> = either {
        noteDao.upsertNote(note.toEntity(waitingForUpload = true)).bind()

        val networkResult = safeNetworkCall {
            httpClient.put(V1.UpdateNote()) { setBody(note.toDto()) }.body<NoteDto>().toNote()
        }

        when (networkResult) {
            is Either.Left -> note
            is Either.Right -> networkResult.value.also { networkNote ->
                noteDao.upsertNote(networkNote.toEntity(waitingForUpload = false)).bind()
            }
        }
    }

    override suspend fun delete(noteId: String): Either<ClientError, Unit> = either {
        val note = noteDao.getNoteById(noteId).bind()

        noteDao.upsertNote(note.copy(waiting_for_delete = true)).bind()

        val networkResult = safeNetworkCall {
            httpClient.delete(V1.DeleteNote(noteId = noteId))
        }

        when (networkResult) {
            is Either.Left -> Unit
            is Either.Right -> noteDao.deleteNoteById(noteId).bind()
        }
    }

    override suspend fun getNoteById(noteId: String): Either<DatabaseError, Note> =
        noteDao.getNoteById(noteId).map(NoteEntity::toNote)

    override suspend fun getCurrentUserNotes(): Either<NetworkError, Unit> = either {
        val notes = safeNetworkCall {
            httpClient.get(V1.GetCurrentUserNotes()).body<List<NoteDto>>().map(NoteDto::toNote)
        }.bind()

        val localNotes = observeLocalUserNotes().firstOrNull()

        notes.forEach { note ->
            val localNote = localNotes?.firstOrNull { it.id == note.id }

            if (localNote?.waitingForDelete == true) return@forEach

            noteDao.upsertNote(note.toEntity())
        }
    }

    override fun observeLocalUserNotes(): Flow<List<Note>> = noteDao.observeAllNotes()
        .map { it.map(NoteEntity::toNote) }

    override fun unsyncedDataExists(): Flow<Boolean> = noteDao.unsyncedDataExists()

    override suspend fun syncNotes() {
        noteDao.observeUnsyncedNotes().firstOrNull()?.forEach { note ->
            when {
                note.waiting_for_delete -> delete(note.id)
                note.waiting_for_upload -> when (note.userId.isEmpty()) {
                    true -> create(note.toNote())
                    false -> update(note.toNote())
                }
            }
        }
    }

    override suspend fun deleteAllLocalNotes() = noteDao.deleteAllNotes()
}
