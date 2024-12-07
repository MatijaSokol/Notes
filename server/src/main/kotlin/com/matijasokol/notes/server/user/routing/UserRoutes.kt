package com.matijasokol.notes.server.user.routing

import arrow.core.raise.either
import com.matijasokol.notes.data.api.V1
import com.matijasokol.notes.data.api.models.UserDto
import com.matijasokol.notes.server.core.routing.receiveOrError
import com.matijasokol.notes.server.core.routing.respond
import com.matijasokol.notes.server.core.routing.tokenOrError
import com.matijasokol.notes.server.user.service.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.resources.post
import io.ktor.server.routing.Routing
import org.koin.ktor.ext.get

fun Routing.userRoutes(
    userService: UserService = get(),
) {
    authenticate {
        post<V1.CreateUser> {
            either {
                val token = call.tokenOrError().bind().token
                val userDto = call.receiveOrError<UserDto>().bind()
                userService.create(userDto).bind()
            }.respond(HttpStatusCode.Created)
        }
    }
}
