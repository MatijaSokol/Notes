package com.matijasokol.notes.server.note.routing

import arrow.core.raise.either
import com.matijasokol.notes.data.api.V1
import com.matijasokol.notes.data.api.models.NoteDto
import com.matijasokol.notes.server.core.routing.receiveOrError
import com.matijasokol.notes.server.core.routing.respond
import com.matijasokol.notes.server.core.routing.tokenOrError
import com.matijasokol.notes.server.note.service.NoteService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.resources.post
import io.ktor.server.routing.Routing
import org.koin.ktor.ext.get
import io.ktor.server.resources.get as getRoute

fun Routing.noteRoutes(
    noteService: NoteService = get(),
) {
    authenticate {
        post<V1.CreateNote> {
            either {
                val token = call.tokenOrError().bind().token
                val noteDto = call.receiveOrError<NoteDto>().bind()
                noteService.create(noteDto).bind()
            }.respond(HttpStatusCode.Created)
        }

        getRoute<V1.GetNote> {
            either {
                val token = call.tokenOrError().bind().token
                noteService.getNote(it.noteId).bind()
            }.respond()
        }

        getRoute<V1.GetNotesByUser> {
            either {
                val token = call.tokenOrError().bind().token
                noteService.getUserNotes(it.userId).bind()
            }.respond()
        }
    }
}
