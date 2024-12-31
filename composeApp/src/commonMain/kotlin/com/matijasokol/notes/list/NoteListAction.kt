package com.matijasokol.notes.list

sealed class NoteListAction {

    data class NavigateToDetails(val noteId: String?) : NoteListAction()
}
