package com.matijasokol.notes.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.matijasokol.notes.ui.components.withSharedBounds
import com.matijasokol.notes.ui.sharedelement.SHARED_ELEMENT_KEY_FAB
import com.matijasokol.notes.ui.sharedelement.buildSharedElementKeyContent
import com.matijasokol.notes.ui.sharedelement.buildSharedElementKeyTitle

@Composable
fun DetailsScreen(
    state: NoteDetailsState,
    modifier: Modifier = Modifier,
    onEvent: (NoteDetailsEvent) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            DetailsTopAppBar(
                title = state.title,
                noteId = state.noteId,
                saveActive = state.saveActive,
                onBackClick = { onEvent(NoteDetailsEvent.OnBackClick) },
                onSaveClick = { onEvent(NoteDetailsEvent.OnSaveClick) },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .then(
                    when (state.noteId == null) {
                        true -> Modifier.withSharedBounds(SHARED_ELEMENT_KEY_FAB)
                        false -> Modifier.withSharedBounds(buildSharedElementKeyContent(state.noteId))
                    },
                )
                .background(
                    color = when (state.noteId == null) {
                        true -> Color.LightGray
                        false -> Color.LightGray
                    },
                ),
        ) {
            BasicTextField(
                value = state.text,
                readOnly = state.saveActive,
                onValueChange = { onEvent(NoteDetailsEvent.OnTextChanged(it)) },
                modifier = Modifier.fillMaxSize().padding(16.dp),
            )
        }
    }
}

@Composable
private fun DetailsTopAppBar(
    title: String?,
    noteId: String?,
    saveActive: Boolean,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = title.orEmpty(),
                modifier = Modifier
                    .then(
                        when (title != null && noteId != null) {
                            true -> Modifier.withSharedBounds(buildSharedElementKeyTitle(noteId))
                            false -> Modifier
                        },
                    ),
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                )
            }
        },
        actions = {
            when (saveActive) {
                true -> CircularProgressIndicator(
                    color = Color.Black,
                    modifier = Modifier.padding(end = 10.dp).size(30.dp),
                    strokeWidth = 3.dp,
                )
                false -> IconButton(
                    onClick = onSaveClick,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Save",
                        tint = Color.Black,
                    )
                }
            }
        },
    )
}
