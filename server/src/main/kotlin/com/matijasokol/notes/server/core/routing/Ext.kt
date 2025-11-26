@file:Suppress("NoUnusedImports", "SpacingAroundColon")
// Add @Suppress because of detekt issue with Context Parameters

package com.matijasokol.notes.server.core.routing

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import arrow.core.toNonEmptyListOrNull
import com.matijasokol.notes.core.error.getCauseOrNull
import com.matijasokol.notes.core.models.InvalidField
import com.matijasokol.notes.server.auth.TokenPrincipal
import com.matijasokol.notes.server.core.DatabaseError
import com.matijasokol.notes.server.core.NoteError
import com.matijasokol.notes.server.core.ServerError
import com.matijasokol.notes.server.core.UserError
import com.matijasokol.notes.server.core.ValidationError
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.auth.principal
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import io.ktor.util.pipeline.PipelineContext
import kotlinx.serialization.MissingFieldException

fun ApplicationCall.tokenOrError(): Either<ValidationError.InvalidToken, TokenPrincipal> =
    either { ensureNotNull(principal()) { ValidationError.InvalidToken } }

suspend inline fun <reified T : Any> ApplicationCall.receiveOrError(): Either<ValidationError.IncorrectInput, T> =
    Either.catchOrThrow<BadRequestException, T> {
        receive<T>()
    }.mapLeft { ex ->
        val missingFieldException = ex.getCauseOrNull<MissingFieldException>() ?: throw ex

        ValidationError.IncorrectInput(
            errors = missingFieldException.missingFields.map { field ->
                InvalidField(
                    field = field,
                    message = "Field is required",
                )
            }.toNonEmptyListOrNull() ?: throw missingFieldException,
        )
    }

context(routingContext: RoutingContext)
suspend inline fun <reified A : Any> Either<ServerError, A>.respond(
    status: HttpStatusCode = HttpStatusCode.OK,
): Unit = when (this) {
    is Either.Left -> routingContext.call.respond(value)
    is Either.Right -> routingContext.call.respond(status, value)
}

suspend fun PipelineContext<Unit, ApplicationCall>.respond(error: ServerError): Unit =
    when (error) {
        is DatabaseError.RecordAlreadyExists -> unprocessable("Record already exists. ${error.message}")
        is DatabaseError.ForeignKeyViolation -> unprocessable("Invalid request. Schema constraints not satisfied.")
        is UserError.UserNotFoundByEmail -> unprocessable("User not found for email: ${error.email}")
        is UserError.UserNotFoundById -> unprocessable("User not found for id: ${error.id}")
        ValidationError.InvalidToken -> unprocessable("Invalid token")
        is ValidationError.IncorrectInput -> unprocessable(
            "Invalid input: ${error.errors.joinToString { field -> "${field.field}: ${field.message}" }}",
        )
        is NoteError.NoteNotFoundById -> unprocessable("Note not found for id: ${error.id}")
    }

private suspend inline fun PipelineContext<Unit, ApplicationCall>.unprocessable(
    error: String,
): Unit = call.respond(
    HttpStatusCode.UnprocessableEntity,
    ErrorResponse(ErrorMessage(listOf(error))),
)

private suspend inline fun PipelineContext<Unit, ApplicationCall>.unprocessable(
    errors: List<String>,
): Unit = call.respond(
    HttpStatusCode.UnprocessableEntity,
    ErrorResponse(ErrorMessage(errors)),
)
