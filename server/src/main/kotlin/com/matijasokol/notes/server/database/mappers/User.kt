package com.matijasokol.notes.server.database.mappers

import com.matijasokol.notes.data.api.models.UserDto
import com.matijasokol.notes.server.UserEntity
import com.matijasokol.notes.server.core.models.value

fun UserEntity.toUserDto() = UserDto(
    id = id.value,
    email = email,
)
