package com.matijasokol.notes.core.models

import arrow.core.Either
import kotlin.uuid.Uuid

@Suppress("FunctionName")
fun Uuid(
    value: String,
    field: String = "UUID",
    message: String = "$field is not valid UUID: $value",
): Either<InvalidField, Uuid> = Either.catch {
    Uuid.parse(value)
}.mapLeft {
    InvalidField(field, message)
}

val Uuid.value: String get() = toString()
