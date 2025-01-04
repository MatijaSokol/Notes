package com.matijasokol.notes.details

sealed class NoteDetailsEvent {

    data object OnBackClick : NoteDetailsEvent()

    data class OnTextChanged(val text: String) : NoteDetailsEvent()

    data class OnTitleChanged(val title: String) : NoteDetailsEvent()

    data class OnSaveClick(
        val title: String,
        val text: String,
    ) : NoteDetailsEvent()
}
