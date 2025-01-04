package com.matijasokol.notes.list

import com.matijasokol.notes.domain.notes.model.Note
import kotlinx.collections.immutable.toPersistentList

class NoteListUiMapper {

    fun toUiState(
        userEmail: String,
        isLoading: Boolean,
        logoutInProgress: Boolean,
        syncStatus: SyncStatus,
        notes: List<Note>,
    ) = NoteListState(
        notes = notes.map(Note::toNoteUi).toPersistentList(),
        errorMessage = null,
        isLoading = isLoading,
        logoutInProgress = logoutInProgress,
        syncStatus = syncStatus,
        email = userEmail,
        emptyListMessage = "No notes found",
    )
}
