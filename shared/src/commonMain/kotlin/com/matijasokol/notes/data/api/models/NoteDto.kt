package com.matijasokol.notes.data.api.models

import kotlinx.serialization.Serializable

@Serializable
data class NoteDto(
    val id: String,
    val title: String,
    val text: String,
    val userId: String,
    val createdAt: Long,
)
