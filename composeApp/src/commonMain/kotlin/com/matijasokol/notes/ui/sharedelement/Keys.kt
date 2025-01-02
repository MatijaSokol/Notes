package com.matijasokol.notes.ui.sharedelement

const val SHARED_ELEMENT_KEY_FAB = "fab"

fun buildSharedElementKeyTitle(noteId: String) = "$noteId-title"

fun buildSharedElementKeyContent(noteId: String) = noteId
