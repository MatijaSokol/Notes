package com.matijasokol.notes.server.database.mappers

import com.matijasokol.notes.data.api.models.NoteDto
import com.matijasokol.notes.server.NoteEntity
import com.matijasokol.notes.server.core.models.value

fun NoteEntity.toNoteDto() = NoteDto(
    id = id.value,
    title = title,
    text = text,
    userId = userId.value,
    createdAt = createdAt,
)
