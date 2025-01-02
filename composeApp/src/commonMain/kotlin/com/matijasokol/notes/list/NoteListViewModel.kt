package com.matijasokol.notes.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.right
import com.matijasokol.notes.data.api.models.NoteDto
import com.matijasokol.notes.domain.auth.AuthProvider
import com.matijasokol.notes.domain.notes.NotesRepository
import com.matijasokol.notes.ui.viewmodel.STOP_TIMEOUT_MILLIS
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteListViewModel(
    private val authProvider: AuthProvider,
    private val notesRepository: NotesRepository,
    private val uiMapper: NoteListUiMapper,
) : ViewModel() {

    private val _actions = Channel<NoteListAction>(capacity = BUFFERED)
    val actions = _actions.receiveAsFlow()

    private val fetchTrigger = Channel<Unit>()
    private val isLoading = MutableStateFlow(true)
    private val logoutInProgress = MutableStateFlow(false)

    private val notes = fetchTrigger.receiveAsFlow()
        .onStart { emit(Unit) }
        .onEach { isLoading.update { true } }
        .map { notesRepository.getCurrentUserNotes() }
        .onEach { isLoading.update { false } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = emptyList<NoteDto>().right(),
        )

    private val userEmail = flow {
        emit(authProvider.getCurrentUserEmail().getOrNull().orEmpty())
    }.onStart { emit("") }

    val state = combine(
        userEmail,
        isLoading,
        logoutInProgress,
        notes,
        uiMapper::toUiState,
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = NoteListState(),
    )

    fun onEvent(event: NoteListEvent) {
        when (event) {
            is NoteListEvent.OnFabClick -> viewModelScope.launch {
                _actions.send(NoteListAction.NavigateToDetails(noteId = null, title = null, text = null))
            }
            is NoteListEvent.OnNoteClick -> viewModelScope.launch {
                _actions.send(
                    NoteListAction.NavigateToDetails(
                        noteId = event.note.id,
                        title = event.note.title,
                        text = event.note.text,
                    ),
                )
            }
            is NoteListEvent.OnNoteDelete -> viewModelScope.launch {
                notesRepository.delete(event.note.id)
            }
            NoteListEvent.OnLogoutClick -> viewModelScope.launch { handleLogout() }
        }
    }

    private suspend fun handleLogout() {
        logoutInProgress.update { true }

        when (authProvider.logout()) {
            is arrow.core.Either.Left -> Unit // handle error
            is arrow.core.Either.Right -> {
                logoutInProgress.update { false }
                _actions.send(NoteListAction.NavigateToAuth)
            }
        }
    }
}
