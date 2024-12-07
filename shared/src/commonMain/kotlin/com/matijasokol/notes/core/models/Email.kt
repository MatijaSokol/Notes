package com.matijasokol.notes.core.models

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import kotlin.jvm.JvmInline

@JvmInline
value class Email private constructor(val value: String) {

    companion object {
        operator fun invoke(
            value: String,
            field: String = "Email",
            message: String = "$field is not valid email: $value",
        ): Either<InvalidField, Email> = either {
            val trimmedEmail = value.trim()
            ensure(trimmedEmail.matches(emailRegex)) { InvalidField(field, message) }
            Email(trimmedEmail)
        }
    }
}

private val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})".toRegex()
