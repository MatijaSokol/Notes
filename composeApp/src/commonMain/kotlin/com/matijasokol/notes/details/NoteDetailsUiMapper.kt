package com.matijasokol.notes.details

class NoteDetailsUiMapper {

    fun toUiState(
        noteId: String?,
        title: String,
        text: String,
        saveActive: Boolean,
    ) = NoteDetailsState(
        noteId = noteId,
        title = title,
        text = text,
        saveActive = saveActive,
    )
}
