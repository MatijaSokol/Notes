package com.matijasokol.notes.details

sealed class NoteDetailsEvent {

    data object OnBackClick : NoteDetailsEvent()

    data class OnTextChanged(val text: String) : NoteDetailsEvent()

    data object OnSaveClick : NoteDetailsEvent()
}
