package com.matijasokol.notes.list

import arrow.core.Either
import com.matijasokol.notes.ClientError
import com.matijasokol.notes.data.api.models.NoteDto
import kotlinx.collections.immutable.toPersistentList

class NoteListUiMapper {

    fun toUiState(
        userEmail: String,
        isLoading: Boolean,
        logoutInProgress: Boolean,
        notesOrError: Either<ClientError, List<NoteDto>>,
    ) = NoteListState(
        notes = notesOrError.getOrNull()?.map(NoteDto::toNoteUi).orEmpty().toPersistentList(),
        errorMessage = notesOrError.leftOrNull()?.let { "Error" },
        isLoading = isLoading,
        logoutInProgress = logoutInProgress,
        email = userEmail,
    )
}
