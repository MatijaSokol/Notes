package com.matijasokol.notes.data

import com.matijasokol.notes.domain.Note
import com.matijasokol.notes.domain.NotesRepository

class NotesRepositoryImpl(
    private val notesService: NotesService,
) : NotesRepository {

    override suspend fun getNotes(): List<Note> = notesService.getNotes().map { it.toNote() }

    override suspend fun getNoteById(noteId: Long): Note? =
        notesService.getNoteById(noteId)?.toNote()

    override suspend fun addNote(note: Note): Note = notesService.addNote(note.toDto()).toNote()

    override suspend fun updateNote(note: Note): Note? =
        notesService.updateNote(note.toDto())?.toNote()

    override suspend fun deleteNoteById(noteId: Long): Boolean =
        notesService.deleteNoteById(noteId)
}
