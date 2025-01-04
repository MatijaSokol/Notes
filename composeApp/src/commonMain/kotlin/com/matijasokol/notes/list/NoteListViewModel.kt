package com.matijasokol.notes.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.matijasokol.notes.domain.auth.AuthProvider
import com.matijasokol.notes.domain.notes.NotesRepository
import com.matijasokol.notes.ui.error.ErrorMapper
import com.matijasokol.notes.ui.viewmodel.STOP_TIMEOUT_MILLIS
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
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
    private val errorMapper: ErrorMapper,
) : ViewModel() {

    private val _actions = Channel<NoteListAction>(capacity = BUFFERED)
    val actions = _actions.receiveAsFlow()

    private val isLoading = MutableStateFlow(true)
    private val logoutInProgress = MutableStateFlow(false)
    private val deleteInProgress = MutableStateFlow(false)

    private val userEmail = flow {
        emit(authProvider.getCurrentUserEmail().getOrNull().orEmpty())
    }.onStart { emit("") }

    private val notes = notesRepository.observeLocalUserNotes()
        // run in separate coroutine to avoid blocking notes flow
        .onStart { viewModelScope.launch { syncNotes() } }
        .onEach { if (it.isNotEmpty()) { isLoading.update { false } } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = emptyList(),
        )

    private val loadFailed = MutableStateFlow(false)
    private val syncInProgress = MutableStateFlow(true)
    private val syncStatus = combine(
        notesRepository.unsyncedDataExists(),
        syncInProgress,
        loadFailed,
        deleteInProgress,
    ) { unSyncedDataExists, syncInProgress, loadFailed, deleteInProgress ->
        when (syncInProgress || deleteInProgress) {
            true -> SyncStatus.SYNCING
            false -> when (unSyncedDataExists || loadFailed) {
                true -> SyncStatus.FAILED
                false -> SyncStatus.SYNCED
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = SyncStatus.SYNCING,
    )

    val state = combine(
        userEmail,
        isLoading,
        logoutInProgress,
        syncStatus,
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
            is NoteListEvent.OnNoteDelete -> viewModelScope.launch { handleDeleteNote(event.note.id) }
            NoteListEvent.OnLogoutClick -> viewModelScope.launch { handleLogout() }
            NoteListEvent.OnSyncClick -> viewModelScope.launch { syncNotes() }
        }
    }

    private suspend fun handleDeleteNote(noteId: String) {
        deleteInProgress.update { true }
        notesRepository.delete(noteId)
        deleteInProgress.update { false }
    }

    private suspend fun handleLogout() {
        logoutInProgress.update { true }

        when (val result = authProvider.logout()) {
            is Either.Left -> {
                logoutInProgress.update { false }
                _actions.send(NoteListAction.ShowMessage(errorMapper.map(result.value)))
            }
            is Either.Right -> {
                logoutInProgress.update { false }
                _actions.send(NoteListAction.NavigateToAuth)
            }
        }
    }

    private suspend fun syncNotes() {
        syncInProgress.update { true }
        loadFailed.update { false }

        val notesUpdateFailed = notesRepository.getCurrentUserNotes()
            .onRight { notesRepository.syncNotes() }
            .isLeft()

        syncInProgress.update { false }
        isLoading.update { false }
        loadFailed.update { notesUpdateFailed }
    }
}
