package com.matijasokol.notes.server.user.service

import arrow.core.Either
import arrow.core.raise.either
import com.matijasokol.notes.data.api.models.UserDto
import com.matijasokol.notes.server.UserEntity
import com.matijasokol.notes.server.core.ServerError
import com.matijasokol.notes.server.database.mappers.toUserDto
import com.matijasokol.notes.server.user.model.toUserOrError
import com.matijasokol.notes.server.user.usecase.CreateUser

class UserServiceImpl(
    private val createUser: CreateUser,
) : UserService {

    override suspend fun create(userDto: UserDto): Either<ServerError, UserDto> = either {
        val user = userDto.toUserOrError().bind()

        createUser(
            id = user.id,
            email = user.email,
        ).map(UserEntity::toUserDto).bind()
    }
}
