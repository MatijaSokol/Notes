package com.matijasokol.notes.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import arrow.core.Either
import com.matijasokol.notes.date.millisNow
import com.matijasokol.notes.domain.notes.NotesRepository
import com.matijasokol.notes.domain.notes.model.Note
import com.matijasokol.notes.navigation.Destination
import com.matijasokol.notes.ui.viewmodel.STOP_TIMEOUT_MILLIS
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
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
    private val notesRepository: NotesRepository,
) : ViewModel() {

    private val _actions = Channel<NoteDetailsAction>(capacity = BUFFERED)
    val actions = _actions.receiveAsFlow()

    private val noteId = savedStateHandle.toRoute<Destination.Details>().noteId
    private val title = savedStateHandle.toRoute<Destination.Details>().title
    private val text = savedStateHandle.toRoute<Destination.Details>().text

    private val titleEdited = MutableStateFlow(title.orEmpty())
    private val textEdited = MutableStateFlow(text.orEmpty())
    private val saveActive = MutableStateFlow(false)

    val state = combine(
        titleEdited,
        textEdited,
        saveActive,
        ::toUiState,
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = NoteDetailsState(
            noteId = noteId,
            title = title.orEmpty(),
            text = text.orEmpty(),
        ),
    )

    fun onEvent(event: NoteDetailsEvent) {
        when (event) {
            is NoteDetailsEvent.OnBackClick -> viewModelScope.launch {
                _actions.send(NoteDetailsAction.NavigateToList)
            }
            is NoteDetailsEvent.OnTextChanged -> textEdited.update { event.text }
            is NoteDetailsEvent.OnSaveClick -> viewModelScope.launch { handleSaveNote(event.title, event.text) }
            is NoteDetailsEvent.OnTitleChanged -> titleEdited.update { event.title }
        }
    }

    private fun toUiState(
        title: String,
        text: String,
        saveActive: Boolean,
    ) = uiMapper.toUiState(
        noteId = noteId,
        title = title,
        text = text,
        saveActive = saveActive,
    )

    private suspend fun buildNote(
        title: String,
        text: String,
    ) = when (noteId) {
        null -> Note(
            id = "",
            title = title,
            text = text,
            userId = "",
            createdAt = millisNow(),
            waitingForUpload = false,
            waitingForDelete = false,
        )
        else -> notesRepository.getNoteById(noteId).getOrNull()
            ?.copy(title = title, text = text)
            ?: error("Handle error")
    }

    private suspend fun handleSaveNote(
        title: String,
        text: String,
    ) {
        saveActive.update { true }

        val note = buildNote(title = title, text = text)
        val result = when (noteId) {
            null -> notesRepository.create(note)
            else -> notesRepository.update(note)
        }

        saveActive.update { false }

        when (result) {
            is Either.Left -> Unit // handle error
            is Either.Right -> _actions.send(NoteDetailsAction.NavigateToList) // handle success
        }
    }
}
