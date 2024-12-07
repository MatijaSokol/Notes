package com.matijasokol.notes.server.user.service

import arrow.core.Either
import com.matijasokol.notes.data.api.models.UserDto
import com.matijasokol.notes.server.core.ServerError

interface UserService {

    suspend fun create(
        userDto: UserDto,
    ): Either<ServerError, UserDto>
}
