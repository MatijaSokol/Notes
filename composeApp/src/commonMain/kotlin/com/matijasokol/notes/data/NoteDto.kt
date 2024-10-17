package com.matijasokol.notes.data

import com.matijasokol.notes.domain.Note

data class NoteDto(
    val id: Long,
    val title: String,
    val content: String,
)

fun NoteDto.toNote() = Note(
    id = id,
    title = title,
    content = content,
)

fun Note.toDto() = NoteDto(
    id = id,
    title = title,
    content = content,
)
