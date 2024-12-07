package com.matijasokol.notes.server.core.routing

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val error: ErrorMessage)

@Serializable
data class ErrorMessage(val body: List<String>)
