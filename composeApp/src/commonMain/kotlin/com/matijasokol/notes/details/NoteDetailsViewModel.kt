package com.matijasokol.notes.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import arrow.core.Either
import arrow.core.raise.either
import arrow.core.right
import com.matijasokol.notes.ClientError
import com.matijasokol.notes.core.models.NonEmptyString
import com.matijasokol.notes.date.millisNow
import com.matijasokol.notes.domain.UUIDProvider
import com.matijasokol.notes.domain.notes.NotesRepository
import com.matijasokol.notes.domain.notes.model.Note
import com.matijasokol.notes.errorAsIncorrectInput
import com.matijasokol.notes.navigation.Destination
import com.matijasokol.notes.ui.error.ErrorMapper
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
    private val uuidProvider: UUIDProvider,
    private val errorMapper: ErrorMapper,
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

    private suspend fun toUiState(
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
    ): Either<ClientError, Note> = either {
        val validTitle = NonEmptyString(value = title, field = "Title").errorAsIncorrectInput().bind()
        val validText = NonEmptyString(value = text, field = "Text").errorAsIncorrectInput().bind()

        when (noteId) {
            null -> Note(
                id = uuidProvider.generateValue(),
                title = validTitle.value,
                text = validText.value,
                userId = "",
                createdAt = millisNow(),
                waitingForUpload = false,
                waitingForDelete = false,
            ).right()
            else -> notesRepository.getNoteById(noteId).map {
                it.copy(title = validTitle.value, text = validText.value)
            }
        }.bind()
    }

    private suspend fun handleSaveNote(
        title: String,
        text: String,
    ) {
        saveActive.update { true }

        val result = either {
            val note = buildNote(title = title, text = text).bind()

            when (noteId) {
                null -> notesRepository.create(note)
                else -> notesRepository.update(note)
            }.bind()
        }

        saveActive.update { false }

        when (result) {
            is Either.Left -> _actions.send(NoteDetailsAction.ShowMessage(errorMapper.map(result.value)))
            is Either.Right -> _actions.send(NoteDetailsAction.NavigateToList)
        }
    }
}
