package com.matijasokol.notes.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.matijasokol.notes.domain.auth.AuthProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authProvider: AuthProvider,
) : ViewModel() {

    private val _actions = Channel<AuthAction>(Channel.BUFFERED)
    val actions = _actions.receiveAsFlow()

    private val email = MutableStateFlow("")
    private val password = MutableStateFlow("")
    private val isLoading = MutableStateFlow(false)

    val state = combine(
        email,
        password,
        isLoading,
        ::AuthState,
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AuthState(),
    )

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.EmailChanged -> email.update { event.email }
            is AuthEvent.PasswordChanged -> password.update { event.password }
            is AuthEvent.LoginClicked -> viewModelScope.launch { handleLogin(event.email, event.password) }
            AuthEvent.GoogleSignInClicked -> TODO()
        }
    }

    private suspend fun handleLogin(
        email: String,
        password: String,
    ) {
        isLoading.update { true }

        when (val result = authProvider.loginWithEmailAndPassword(email, password)) {
            is Either.Left -> {
                _actions.send(AuthAction.LoginError)
                when (result.value) {
                    com.matijasokol.notes.LoginError.InvalidCredentials -> println("Invalid credentials")
                    is com.matijasokol.notes.NetworkError.BackendError -> println("Backend error")
                    com.matijasokol.notes.NetworkError.UnknownNetworkError -> println("Unknown network error")
                }
            }
            is Either.Right -> {
                println(result.value)
                _actions.send(AuthAction.LoginSuccess)
            }
        }

        isLoading.update { false }
    }
}
