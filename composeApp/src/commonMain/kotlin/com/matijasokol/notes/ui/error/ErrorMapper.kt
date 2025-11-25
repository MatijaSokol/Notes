package com.matijasokol.notes.ui.error

import com.matijasokol.notes.AuthError
import com.matijasokol.notes.ClientError
import com.matijasokol.notes.DatabaseError
import com.matijasokol.notes.LoginError
import com.matijasokol.notes.NetworkError
import com.matijasokol.notes.NoteDatabaseError
import com.matijasokol.notes.RegistrationError
import com.matijasokol.notes.ValidationError
import com.matijasokol.notes.ui.dictionary.Dictionary
import notes.composeapp.generated.resources.Res
import notes.composeapp.generated.resources.error_backend
import notes.composeapp.generated.resources.error_email_not_available
import notes.composeapp.generated.resources.error_generic
import notes.composeapp.generated.resources.error_incorrect_input
import notes.composeapp.generated.resources.error_invalid_credentials
import notes.composeapp.generated.resources.error_network
import notes.composeapp.generated.resources.error_note_not_found
import notes.composeapp.generated.resources.error_registration_failed
import notes.composeapp.generated.resources.error_token_not_available

class ErrorMapper(private val dictionary: Dictionary) {

    suspend fun map(error: ClientError) = with(dictionary) {
        when (error) {
            AuthError.EmailNotAvailable -> getString(Res.string.error_email_not_available.key)
            AuthError.TokenNotAvailable -> getString(Res.string.error_token_not_available.key)
            DatabaseError.GenericError -> getString(Res.string.error_generic.key)
            is NoteDatabaseError.NoteNotFound -> getString(Res.string.error_note_not_found.key)
            LoginError.InvalidCredentials -> getString(Res.string.error_invalid_credentials.key)
            is NetworkError.BackendError -> getString(Res.string.error_backend.key)
            NetworkError.UnknownNetworkError -> getString(Res.string.error_network.key)
            RegistrationError.RegistrationFailed -> getString(Res.string.error_registration_failed.key)
            is ValidationError.IncorrectInput -> getString(Res.string.error_incorrect_input.key, error.error.field)
        }
    }
}
