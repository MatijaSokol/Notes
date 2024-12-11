package com.matijasokol.notes.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.getOrElse
import com.matijasokol.notes.domain.auth.AuthProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class SplashViewModel(
    private val authProvider: AuthProvider,
) : ViewModel() {

    private val fetchTrigger = Channel<Unit>()
    val loggedIn = fetchTrigger.receiveAsFlow()
        .onStart { emit(Unit) }
        .map { authProvider.userLoggedIn().getOrElse { false } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )
}
