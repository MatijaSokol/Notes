package com.matijasokol.notes.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {

  @Serializable
  data object List : Destination

  @Serializable
  data class Details(val noteId: Int) : Destination
}
