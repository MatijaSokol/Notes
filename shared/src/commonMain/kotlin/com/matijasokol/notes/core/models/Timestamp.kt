package com.matijasokol.notes.core.models

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import kotlin.jvm.JvmInline

@JvmInline
value class Timestamp private constructor(val value: Long) {

    companion object {
        operator fun invoke(
            value: Long,
            field: String = "Timestamp",
            message: String = "$field is not valid timestamp: $value",
        ): Either<InvalidField, Timestamp> = either {
            ensure(value.toString().length == TIMESTAMP_LENGTH) { InvalidField(field, message) }
            Timestamp(value)
        }
    }
}

private const val TIMESTAMP_LENGTH = 13
