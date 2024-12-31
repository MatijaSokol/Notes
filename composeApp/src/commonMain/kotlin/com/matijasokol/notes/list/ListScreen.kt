package com.matijasokol.notes.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.matijasokol.notes.ui.components.withSharedElement
import com.matijasokol.notes.ui.swipetodismiss.SwipeToDeleteContainer
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ListScreen(
    state: NoteListState,
    modifier: Modifier = Modifier,
    onEvent: (NoteListEvent) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("List screen") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.withSharedElement("fab"),
                onClick = { onEvent(NoteListEvent.OnFabClick) },
                containerColor = Color.Red,
            ) {
                Text("+")
            }
        },
    ) { innerPadding ->
        when (state.isLoading) {
            true -> LoadingScreen(modifier = Modifier.padding(innerPadding))
            false -> ListScreen(
                modifier = Modifier.padding(innerPadding),
                items = state.notes,
                onEvent = onEvent,
            )
        }
    }
}

@Composable
private fun LoadingScreen(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ListScreen(
    modifier: Modifier = Modifier,
    items: ImmutableList<NoteUi>,
    onEvent: (NoteListEvent) -> Unit,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(
            items = items,
            key = NoteUi::id,
        ) { note ->
            SwipeToDeleteContainer(
                item = note,
                onDelete = { onEvent(NoteListEvent.OnNoteDelete(note)) },
            ) {
                NoteItem(
                    note = note,
                    onClick = { onEvent(NoteListEvent.OnNoteClick(note)) },
                )
            }
        }
    }
}

@Composable
private fun NoteItem(
    note: NoteUi,
    modifier: Modifier = Modifier,
    onClick: (NoteUi) -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(8.dp),
        onClick = { onClick(note) },
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
        ) {
            Text(text = note.text)
            Text(text = note.createdAt)
        }
    }
}
