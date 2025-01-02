package com.matijasokol.notes.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {

    @Serializable
    data object Auth : Destination

    @Serializable
    data object List : Destination

    @Serializable
    data class Details(
        val title: String?,
        val noteId: String?,
        val text: String?,
    ) : Destination
}
