package com.matijasokol.notes.navigation

import kotlinx.coroutines.flow.Flow

interface Navigator {

    val navigationEvent: Flow<NavigationEvent>

    suspend fun emitDestination(event: NavigationEvent)

    fun tryEmitDestination(event: NavigationEvent)
}
