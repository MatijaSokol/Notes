package com.matijasokol.notes.details

import com.matijasokol.notes.ui.dictionary.Dictionary
import notes.composeapp.generated.resources.Res
import notes.composeapp.generated.resources.details_title_label

class NoteDetailsUiMapper(
    private val dictionary: Dictionary,
) {

    suspend fun toUiState(
        noteId: String?,
        title: String,
        text: String,
        saveActive: Boolean,
    ) = NoteDetailsState(
        noteId = noteId,
        title = title,
        text = text,
        saveActive = saveActive,
        titleLabel = dictionary.getString(Res.string.details_title_label.key),
    )
}
