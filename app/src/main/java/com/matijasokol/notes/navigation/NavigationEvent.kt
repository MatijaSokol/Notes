package com.matijasokol.notes.navigation

import androidx.navigation.NavOptionsBuilder

sealed interface NavigationEvent {

    data object NavigateUp : NavigationEvent

    data class Destination(
        val destination: String,
        val builder: NavOptionsBuilder.() -> Unit = {},
    ) : NavigationEvent
}
