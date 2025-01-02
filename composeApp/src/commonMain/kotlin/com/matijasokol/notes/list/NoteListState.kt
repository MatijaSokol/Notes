package com.matijasokol.notes.list

import com.matijasokol.notes.data.api.models.NoteDto
import com.matijasokol.notes.date.timestampToDateFormatted
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class NoteListState(
    val query: String = "",
    val notes: ImmutableList<NoteUi> = persistentListOf(),
    val errorMessage: String? = null,
    val isLoading: Boolean = true,
    val logoutInProgress: Boolean = false,
)

data class NoteUi(
    val id: String,
    val title: String,
    val text: String,
    val createdAt: String,
)

fun NoteDto.toNoteUi() = NoteUi(
    id = id,
    title = title,
    text = text,
    createdAt = timestampToDateFormatted(createdAt),
)
