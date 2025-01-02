package com.matijasokol.notes.domain.notes.model

import com.matijasokol.notes.client.NoteEntity
import com.matijasokol.notes.data.api.models.NoteDto

data class Note(
    val id: String,
    val title: String,
    val text: String,
    val userId: String,
    val createdAt: Long,
    val waitingForDelete: Boolean,
    val waitingForUpload: Boolean,
)

fun NoteDto.toNote() = Note(
    id = id,
    title = title,
    text = text,
    userId = userId,
    createdAt = createdAt,
    waitingForDelete = false,
    waitingForUpload = false,
)

fun NoteEntity.toNote() = Note(
    id = id,
    title = title,
    text = text,
    userId = userId,
    createdAt = created_at,
    waitingForDelete = waiting_for_delete,
    waitingForUpload = waiting_for_upload,
)

fun Note.toEntity(
    waitingForUpload: Boolean = false,
    waitingForDelete: Boolean = false,
) = NoteEntity(
    id = id,
    title = title,
    text = text,
    userId = userId,
    created_at = createdAt,
    waiting_for_delete = waitingForDelete,
    waiting_for_upload = waitingForUpload,
)

fun Note.toDto() = NoteDto(
    id = id,
    title = title,
    text = text,
    userId = userId,
    createdAt = createdAt,
)
