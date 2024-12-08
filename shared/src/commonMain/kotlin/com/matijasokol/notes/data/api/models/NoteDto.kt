package com.matijasokol.notes.data.api.models

data class NoteDto(
    val id: String,
    val text: String,
    val userId: String,
    val createdAt: Long,
)
