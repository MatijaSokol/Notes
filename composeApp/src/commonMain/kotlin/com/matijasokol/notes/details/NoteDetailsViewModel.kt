package com.matijasokol.notes.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.matijasokol.notes.navigation.Destination
import com.matijasokol.notes.ui.viewmodel.STOP_TIMEOUT_MILLIS
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn

class NoteDetailsViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val fetchTrigger = Channel<Unit>()

    val state = fetchTrigger.receiveAsFlow()
        .onStart { emit(Unit) }
        .map { savedStateHandle.toRoute<Destination.Details>().noteId ?: "" }
        .map(::NoteDetailsState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = NoteDetailsState(),
        )
}
