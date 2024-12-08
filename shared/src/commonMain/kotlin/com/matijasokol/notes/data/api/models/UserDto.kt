package com.matijasokol.notes.data.api.models

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val email: String,
)
