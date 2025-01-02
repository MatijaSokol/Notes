package com.matijasokol.notes.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.matijasokol.notes.navigation.Destination
import com.matijasokol.notes.ui.viewmodel.STOP_TIMEOUT_MILLIS
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val uiMapper: NoteDetailsUiMapper,
) : ViewModel() {

    private val _actions = Channel<NoteDetailsAction>(capacity = BUFFERED)
    val actions = _actions.receiveAsFlow()

    private val noteId = savedStateHandle.toRoute<Destination.Details>().noteId
    private val title = savedStateHandle.toRoute<Destination.Details>().title
    private val text = savedStateHandle.toRoute<Destination.Details>().text

    private val textEdited = MutableStateFlow(text.orEmpty())
    private val saveActive = MutableStateFlow(false)

    val state = combine(
        textEdited,
        saveActive,
        ::toUiState,
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = NoteDetailsState(
            noteId = noteId,
            title = title,
            text = text.orEmpty(),
        ),
    )

    fun onEvent(event: NoteDetailsEvent) {
        when (event) {
            is NoteDetailsEvent.OnBackClick -> viewModelScope.launch {
                _actions.send(NoteDetailsAction.NavigateToList)
            }
            is NoteDetailsEvent.OnTextChanged -> textEdited.update { event.text }
            NoteDetailsEvent.OnSaveClick -> viewModelScope.launch { handleSaveNote() }
        }
    }

    private fun toUiState(
        text: String,
        saveActive: Boolean,
    ) = uiMapper.toUiState(
        noteId = noteId,
        title = title,
        text = text,
        saveActive = saveActive,
    )

    private suspend fun handleSaveNote() {
        saveActive.update { true }
        delay(500)
        saveActive.update { false }
        _actions.send(NoteDetailsAction.NavigateToList)
    }
}
