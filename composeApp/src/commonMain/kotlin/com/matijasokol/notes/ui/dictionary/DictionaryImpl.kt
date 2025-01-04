package com.matijasokol.notes.ui.dictionary

import notes.composeapp.generated.resources.Res
import notes.composeapp.generated.resources.allStringResources

class DictionaryImpl : Dictionary {

    override suspend fun getString(resKey: String): String = Res.allStringResources[resKey]
        ?.let { stringRes -> org.jetbrains.compose.resources.getString(stringRes) }
        ?: ""

    override suspend fun getString(
        resKey: String,
        vararg args: Any,
    ): String = Res.allStringResources[resKey]
        ?.let { stringRes -> org.jetbrains.compose.resources.getString(resource = stringRes, formatArgs = args) }
        ?: ""
}
