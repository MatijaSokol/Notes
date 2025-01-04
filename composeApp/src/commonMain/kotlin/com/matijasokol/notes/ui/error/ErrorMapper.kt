package com.matijasokol.notes.ui.error

import com.matijasokol.notes.ClientError
import com.matijasokol.notes.ui.dictionary.Dictionary
import notes.composeapp.generated.resources.Res
import notes.composeapp.generated.resources.error_backend
import notes.composeapp.generated.resources.error_email_not_available
import notes.composeapp.generated.resources.error_generic
import notes.composeapp.generated.resources.error_invalid_credentials
import notes.composeapp.generated.resources.error_network
import notes.composeapp.generated.resources.error_note_not_found
import notes.composeapp.generated.resources.error_registration_failed
import notes.composeapp.generated.resources.error_token_not_available

class ErrorMapper(
    private val dictionary: Dictionary,
) {

    suspend fun map(error: ClientError) = with(Res.string) {
        when (error) {
            com.matijasokol.notes.AuthError.EmailNotAvailable -> error_email_not_available
            com.matijasokol.notes.AuthError.TokenNotAvailable -> error_token_not_available
            com.matijasokol.notes.DatabaseError.GenericError -> error_generic
            is com.matijasokol.notes.NoteDatabaseError.NoteNotFound -> error_note_not_found
            com.matijasokol.notes.LoginError.InvalidCredentials -> error_invalid_credentials
            is com.matijasokol.notes.NetworkError.BackendError -> error_backend
            com.matijasokol.notes.NetworkError.UnknownNetworkError -> error_network
            com.matijasokol.notes.RegistrationError.RegistrationFailed -> error_registration_failed
        }.key.let { dictionary.getString(it) }
    }
}
