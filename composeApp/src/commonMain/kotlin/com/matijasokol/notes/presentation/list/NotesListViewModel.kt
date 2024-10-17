package com.matijasokol.notes.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matijasokol.notes.domain.NotesRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class NotesListViewModel(
    private val notesRepository: NotesRepository,
    private val notesListUiMapper: NotesListUiMapper,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _fetchTrigger = Channel<Unit>(capacity = BUFFERED)
    private val notes = _fetchTrigger.receiveAsFlow()
        .onStart { emit(Unit) }
        .onEach { _isLoading.update { true } }
        .map { notesRepository.getNotes() }
        .onEach { _isLoading.update { false } }

    val state = combine(
        _isLoading,
        notes,
        notesListUiMapper::toUiState,
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = NotesListUiState(),
    )
}
