package com.matijasokol.notes.list

sealed class NoteListAction {

    data class NavigateToDetails(
        val noteId: String?,
        val title: String?,
        val text: String?,
    ) : NoteListAction()

    data object NavigateToAuth : NoteListAction()
}
