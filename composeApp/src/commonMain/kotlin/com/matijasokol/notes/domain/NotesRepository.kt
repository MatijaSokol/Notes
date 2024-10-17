package com.matijasokol.notes.domain

interface NotesRepository {

    suspend fun getNotes(): List<Note>

    suspend fun getNoteById(noteId: Long): Note?

    suspend fun addNote(note: Note): Note

    suspend fun updateNote(note: Note): Note?

    suspend fun deleteNoteById(noteId: Long): Boolean
}
