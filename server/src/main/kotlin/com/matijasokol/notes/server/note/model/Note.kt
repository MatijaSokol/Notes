package com.matijasokol.notes.server.note.model

import arrow.core.Either
import com.matijasokol.notes.core.models.NonEmptyString
import com.matijasokol.notes.core.models.Timestamp
import com.matijasokol.notes.core.models.Uuid
import com.matijasokol.notes.data.api.models.NoteDto
import com.matijasokol.notes.server.core.ValidationError
import kotlin.uuid.Uuid

data class Note(
    val id: Uuid,
    val title: NonEmptyString,
    val text: NonEmptyString,
    val userId: Uuid,
    val createdAt: Timestamp,
) {

    companion object {
        operator fun invoke(
            id: String,
            title: String,
            text: String,
            userId: String,
            createdAt: Long,
        ): Either<ValidationError.IncorrectInput, Note> = Either.zipOrAccumulate(
            Uuid(value = id, field = "Id"),
            NonEmptyString(value = title, field = "Title"),
            NonEmptyString(value = text, field = "Text"),
            Uuid(value = userId, field = "UserId"),
            Timestamp(value = createdAt, field = "CreatedAt"),
            ::Note,
        ).mapLeft(ValidationError::IncorrectInput)
    }
}

fun NoteDto.toNoteOrError(): Either<ValidationError.IncorrectInput, Note> = Note(
    id = id,
    title = title,
    text = text,
    userId = userId,
    createdAt = createdAt,
)
