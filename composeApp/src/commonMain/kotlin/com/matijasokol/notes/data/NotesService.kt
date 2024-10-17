package com.matijasokol.notes.data

interface NotesService {

    suspend fun getNotes(): List<NoteDto>

    suspend fun getNoteById(noteId: Long): NoteDto?

    suspend fun addNote(note: NoteDto): NoteDto

    suspend fun updateNote(note: NoteDto): NoteDto?

    suspend fun deleteNoteById(noteId: Long): Boolean
}
