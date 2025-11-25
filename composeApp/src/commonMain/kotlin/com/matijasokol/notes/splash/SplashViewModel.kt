package com.matijasokol.notes.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.getOrElse
import com.matijasokol.notes.domain.auth.AuthProvider
import com.matijasokol.notes.ui.viewmodel.STOP_TIMEOUT_MILLIS
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class SplashViewModel(private val authProvider: AuthProvider) : ViewModel() {

    val loggedIn = flow {
        emit(authProvider.userLoggedIn().getOrElse { false })
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = null,
    )
}
