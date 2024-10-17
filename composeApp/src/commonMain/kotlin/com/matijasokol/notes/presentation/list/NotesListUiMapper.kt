package com.matijasokol.notes.presentation.list

import com.matijasokol.notes.core.presentation.Dictionary
import com.matijasokol.notes.domain.Note

class NotesListUiMapper(
    private val dictionary: Dictionary,
) {

    fun toUiState(
        isLoading: Boolean,
        notes: List<Note>,
    ): NotesListUiState = NotesListUiState(
        isLoading = isLoading,
        notes = notes.map { it.toUi() },
    )
}
