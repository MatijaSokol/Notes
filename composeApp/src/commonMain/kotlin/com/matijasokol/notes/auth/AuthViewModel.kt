package com.matijasokol.notes.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.matijasokol.notes.LoginError
import com.matijasokol.notes.NetworkError
import com.matijasokol.notes.RegistrationError
import com.matijasokol.notes.auth.AuthType.Login
import com.matijasokol.notes.auth.AuthType.Registration
import com.matijasokol.notes.domain.auth.AuthProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authProvider: AuthProvider,
) : ViewModel() {

    private val _actions = Channel<AuthAction>(Channel.BUFFERED)
    val actions = _actions.receiveAsFlow()

    private val loginEmail = MutableStateFlow("")
    private val loginPassword = MutableStateFlow("")
    private val registrationEmail = MutableStateFlow("")
    private val registrationPassword = MutableStateFlow("")

    private val isLoading = MutableStateFlow(false)
    private val passwordVisible = MutableStateFlow(false)
    private val authType = MutableStateFlow(Login)

    private val email = authType.flatMapLatest { type ->
        when (type) {
            Login -> loginEmail
            Registration -> registrationEmail
        }
    }
    private val password = authType.flatMapLatest { type ->
        when (type) {
            Login -> loginPassword
            Registration -> registrationPassword
        }
    }

    val state = combine(
        email,
        password,
        passwordVisible,
        isLoading,
        authType,
        ::AuthState,
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AuthState(),
    )

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.EmailChanged -> when (event.authType) {
                Login -> loginEmail.update { event.email }
                Registration -> registrationEmail.update { event.email }
            }
            is AuthEvent.PasswordChanged -> when (event.authType) {
                Login -> loginPassword.update { event.password }
                Registration -> registrationPassword.update { event.password }
            }
            AuthEvent.TogglePasswordVisibility -> passwordVisible.update { !it }
            is AuthEvent.LoginClicked -> viewModelScope.launch { handleLogin(event.email, event.password) }
            AuthEvent.GoogleSignInClicked -> Unit
            AuthEvent.ToggleAuthType -> authType.update { it.toggle() }
            is AuthEvent.RegistrationClicked -> viewModelScope.launch { handleRegistration(event.email, event.password) }
        }
    }

    private suspend fun handleRegistration(email: String, password: String) {
        isLoading.update { true }

        when (val result = authProvider.registerWithEmailAndPassword(email, password)) {
            is Either.Left -> {
                _actions.send(AuthAction.RegistrationError)
                when (result.value) {
                    LoginError.InvalidCredentials -> println("Invalid credentials")
                    is NetworkError.BackendError -> println("Backend error")
                    NetworkError.UnknownNetworkError -> println("Unknown network error")
                    RegistrationError.RegistrationFailed -> println("Registration failed")
                }
            }
            is Either.Right -> {
                println(result.value)
                _actions.send(AuthAction.RegistrationSuccess)
            }
        }

        isLoading.update { false }
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
                    LoginError.InvalidCredentials -> println("Invalid credentials")
                    is NetworkError.BackendError -> println("Backend error")
                    NetworkError.UnknownNetworkError -> println("Unknown network error")
                    RegistrationError.RegistrationFailed -> println("Registration failed")
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
