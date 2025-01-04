package com.matijasokol.notes.list

import com.matijasokol.notes.date.timestampToDateFormatted
import com.matijasokol.notes.domain.notes.model.Note
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class NoteListState(
    val query: String = "",
    val notes: ImmutableList<NoteUi> = persistentListOf(),
    val errorMessage: String? = null,
    val emptyListMessage: String = "",
    val isLoading: Boolean = true,
    val logoutInProgress: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.SYNCING,
    val email: String = "",
)

data class NoteUi(
    val id: String,
    val title: String,
    val text: String,
    val createdAt: String,
    val synced: Boolean,
)

fun Note.toNoteUi() = NoteUi(
    id = id,
    title = title,
    text = text,
    createdAt = timestampToDateFormatted(createdAt),
    synced = !waitingForDelete && !waitingForUpload,
)
