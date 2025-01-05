package com.matijasokol.notes.domain.user

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import com.matijasokol.notes.ClientError
import com.matijasokol.notes.RegistrationError
import com.matijasokol.notes.core.models.Email
import com.matijasokol.notes.core.models.NonEmptyString
import com.matijasokol.notes.data.api.models.UserDto
import com.matijasokol.notes.domain.UUIDProvider
import com.matijasokol.notes.domain.auth.AuthProvider

class RegisterUser(
    private val userRepository: UserRepository,
    private val authProvider: AuthProvider,
    private val uuidProvider: UUIDProvider,
) {

    suspend operator fun invoke(
        email: Email,
        password: NonEmptyString,
    ): Either<ClientError, UserDto> = either {
        val authUser = authProvider.registerWithEmailAndPassword(email, password).bind()
        val authEmail = ensureNotNull(authUser.email) { RegistrationError.RegistrationFailed }

        userRepository.createUser(
            UserDto(
                id = uuidProvider.generateValue(), // Actually, server generates the ID so we could pass empty string here
                email = authEmail,
            ),
        ).bind()
    }
}
