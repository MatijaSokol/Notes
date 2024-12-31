package com.matijasokol.notes.data.notes

import arrow.core.Either
import com.matijasokol.notes.NetworkError
import com.matijasokol.notes.data.api.V1
import com.matijasokol.notes.data.api.models.NoteDto
import com.matijasokol.notes.data.safeNetworkCall
import com.matijasokol.notes.domain.notes.NotesRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.delete
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody

class NotesRepositoryImpl(
    private val httpClient: HttpClient,
) : NotesRepository {

    override suspend fun create(note: NoteDto): Either<NetworkError, NoteDto> = safeNetworkCall {
        httpClient.post(V1.CreateNote()) { setBody(note) }.body()
    }

    override suspend fun delete(noteId: String): Either<NetworkError, Unit> = safeNetworkCall {
        httpClient.delete(V1.DeleteNote(noteId = noteId))
    }

    override suspend fun getNoteById(noteId: String): Either<NetworkError, NoteDto> =
        safeNetworkCall { httpClient.get(V1.GetNote(noteId = noteId)).body() }

    override suspend fun getCurrentUserNotes(): Either<NetworkError, List<NoteDto>> =
        safeNetworkCall { httpClient.get(V1.GetCurrentUserNotes()).body() }
}
