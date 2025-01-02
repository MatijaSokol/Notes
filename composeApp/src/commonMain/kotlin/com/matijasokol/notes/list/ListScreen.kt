package com.matijasokol.notes.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.matijasokol.notes.ui.components.withSharedBounds
import com.matijasokol.notes.ui.sharedelement.SHARED_ELEMENT_KEY_FAB
import com.matijasokol.notes.ui.sharedelement.buildSharedElementKeyContent
import com.matijasokol.notes.ui.sharedelement.buildSharedElementKeyTitle
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
            ListTopAppBar(
                state = state,
                onSyncClick = { onEvent(NoteListEvent.OnSyncClick) },
                onLogoutClick = { onEvent(NoteListEvent.OnLogoutClick) },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.withSharedBounds(SHARED_ELEMENT_KEY_FAB),
                onClick = { onEvent(NoteListEvent.OnFabClick) },
                containerColor = Color.DarkGray,
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
                content = {
                    NoteItem(
                        modifier = Modifier
                            .withSharedBounds(buildSharedElementKeyContent(note.id))
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onEvent(NoteListEvent.OnNoteClick(note)) },
                        note = note,
                    )
                },
            )
        }
    }
}

@Composable
private fun NoteItem(
    note: NoteUi,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(
                text = note.title,
                modifier = Modifier.withSharedBounds(buildSharedElementKeyTitle(note.id)),
            )
        },
        supportingContent = { Text(text = note.text) },
        trailingContent = { Text(text = note.createdAt) },
        colors = ListItemDefaults.colors(containerColor = Color.LightGray),
    )
}

@Composable
private fun ListTopAppBar(
    state: NoteListState,
    modifier: Modifier = Modifier,
    onSyncClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(state.email) },
        actions = {
            if (state.unsyncedData) {
                IconButton(
                    onClick = onSyncClick,
                ) {
                    Icon(
                        imageVector = Icons.Filled.CloudOff,
                        contentDescription = "Sync",
                        tint = Color.Black,
                    )
                }
            }

            when (state.logoutInProgress) {
                true -> CircularProgressIndicator(
                    color = Color.Black,
                    modifier = Modifier.padding(end = 10.dp).size(30.dp),
                    strokeWidth = 3.dp,
                )
                false -> IconButton(
                    onClick = onLogoutClick,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Logout",
                        tint = Color.Black,
                    )
                }
            }
        },
    )
}
