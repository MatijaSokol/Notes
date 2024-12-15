package com.matijasokol.notes.domain.user

import arrow.core.Either
import com.matijasokol.notes.NetworkError
import com.matijasokol.notes.data.api.models.UserDto

interface UserRepository {

    suspend fun createUser(user: UserDto): Either<NetworkError, UserDto>
}
