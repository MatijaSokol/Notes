package com.matijasokol.notes.core.models

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import kotlin.jvm.JvmInline

@JvmInline
value class NonEmptyString private constructor(val value: String) {

    companion object {
        operator fun invoke(
            value: String,
            field: String = "Text",
            message: String = "Text is empty",
        ): Either<InvalidField, NonEmptyString> = either {
            ensure(value.isNotBlank()) { InvalidField(field, message) }
            NonEmptyString(value)
        }
    }
}
