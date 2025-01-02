package com.matijasokol.notes.details

data class NoteDetailsState(
    val noteId: String? = null,
    val title: String? = null,
    val text: String = "",
    val saveActive: Boolean = false,
)
