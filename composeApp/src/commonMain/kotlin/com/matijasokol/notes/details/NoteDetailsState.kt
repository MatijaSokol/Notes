package com.matijasokol.notes.details

data class NoteDetailsState(
    val noteId: String? = null,
    val title: String = "",
    val text: String = "",
    val saveActive: Boolean = false,
    val titleLabel: String = "",
)
