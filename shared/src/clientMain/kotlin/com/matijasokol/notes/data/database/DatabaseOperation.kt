package com.matijasokol.notes.data.database

import arrow.core.Either
import com.matijasokol.notes.AppDispatchers
import com.matijasokol.notes.DatabaseError
import kotlinx.coroutines.withContext

suspend inline fun <T> databaseOperation(
    appDispatchers: AppDispatchers,
    crossinline action: () -> T,
): Either<DatabaseError, T> = withContext(appDispatchers.io) {
    Either.catchOrThrow<Exception, T> {
        action()
    }.mapLeft {
        DatabaseError.GenericError
    }
}
