package com.matijasokol.notes.data.user

import arrow.core.Either
import com.matijasokol.notes.NetworkError
import com.matijasokol.notes.data.api.V1
import com.matijasokol.notes.data.api.models.UserDto
import com.matijasokol.notes.data.safeNetworkCall
import com.matijasokol.notes.domain.user.UserRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody

class UserRepositoryImpl(private val client: HttpClient) : UserRepository {

    override suspend fun createUser(user: UserDto): Either<NetworkError, UserDto> = safeNetworkCall {
        client.post(V1.CreateUser()) { setBody(user) }.body()
    }
}
