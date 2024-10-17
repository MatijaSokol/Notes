package com.matijasokol.notes.presentation.list

import com.matijasokol.notes.domain.Note

data class NotesListUiState(
    val isLoading: Boolean = true,
    val notes: List<NoteUi> = emptyList(),
)

data class NoteUi(
    val id: Long,
    val title: String,
    val content: String,
)

fun Note.toUi() = NoteUi(
    id = id,
    title = title,
    content = content,
)
