package com.matijasokol.notes.data

class FakeNotesService : NotesService {

    private val notes = mutableListOf(
        NoteDto(1, "First note", "This is the first note"),
        NoteDto(2, "Second note", "This is the second note"),
        NoteDto(3, "Third note", "This is the third note"),
        NoteDto(4, "Fourth note", "This is the fourth note"),
        NoteDto(5, "Fifth note", "This is the fifth note"),
    )

    override suspend fun getNotes(): List<NoteDto> = notes

    override suspend fun getNoteById(noteId: Long): NoteDto? = notes.find { it.id == noteId }

    override suspend fun addNote(note: NoteDto): NoteDto {
        val newNote = note.copy(id = notes.size.toLong() + 1)
        notes.add(newNote)
        return newNote
    }

    override suspend fun updateNote(note: NoteDto): NoteDto? {
        val index = notes.indexOfFirst { it.id == note.id }
        if (index == -1) return null
        notes[index] = note
        return note
    }

    override suspend fun deleteNoteById(noteId: Long): Boolean {
        val index = notes.indexOfFirst { it.id == noteId }
        if (index == -1) return false
        notes.removeAt(index)
        return true
    }
}
