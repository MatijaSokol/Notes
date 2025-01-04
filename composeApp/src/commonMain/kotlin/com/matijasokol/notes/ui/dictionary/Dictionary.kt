package com.matijasokol.notes.ui.dictionary

interface Dictionary {

    suspend fun getString(resKey: String): String

    suspend fun getString(
        resKey: String,
        vararg args: Any,
    ): String
}
