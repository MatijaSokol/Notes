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
)

data class NoteUi(
    val id: String,
    val text: String,
    val createdAt: String,
)

fun NoteDto.toNoteUi() = NoteUi(
    id = id,
    text = text,
    createdAt = timestampToDateFormatted(createdAt),
)
