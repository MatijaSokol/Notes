package com.matijasokol.notes.list

import com.matijasokol.notes.domain.notes.model.Note
import com.matijasokol.notes.ui.dictionary.Dictionary
import kotlinx.collections.immutable.toPersistentList
import notes.composeapp.generated.resources.Res
import notes.composeapp.generated.resources.list_empty

class NoteListUiMapper(
    private val dictionary: Dictionary,
) {

    suspend fun toUiState(
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
        emptyListMessage = dictionary.getString(Res.string.list_empty.key),
    )
}
