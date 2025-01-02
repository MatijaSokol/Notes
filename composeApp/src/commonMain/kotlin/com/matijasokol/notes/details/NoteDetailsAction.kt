package com.matijasokol.notes.details

sealed class NoteDetailsAction {

    data object NavigateToList : NoteDetailsAction()
}
