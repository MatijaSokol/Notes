package com.matijasokol.notes.list

sealed class NoteListEvent {

    data object OnFabClick : NoteListEvent()

    data class OnNoteClick(val note: NoteUi) : NoteListEvent()

    data class OnNoteDelete(val note: NoteUi) : NoteListEvent()
}
