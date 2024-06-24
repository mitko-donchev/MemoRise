@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.mitko.memorise.presentation.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.mitko.domain.note.Note
import com.mitko.memorise.presentation.ui.components.HomeAppBar
import com.mitko.memorise.presentation.ui.components.NoteItem
import com.mitko.memorise.presentation.ui.navigation.AppNavigationActions
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeView(
    uiState: HomeViewState,
    navigationActions: AppNavigationActions,
    selectNote: (Int) -> Unit,
    selectAllNotes: (Boolean) -> Job,
    updateNote: (Note) -> Job,
    deleteNote: (Int) -> Job,
    deleteNotes: (List<Int>) -> Job,
    deleteAllNotes: () -> Job
) {
    val snackbarHostState = remember { SnackbarHostState() }

    var backPressState by remember { mutableStateOf(BackPressState.InitialPress) }

    var areAllSelected: Boolean by remember { mutableStateOf(false) }
    var showSnack by remember { mutableStateOf(false) }
    var noteListIsEmpty: Boolean by remember { mutableStateOf(true) }
    var noteListHasSelectedItems: Boolean by remember { mutableStateOf(true) }

    var selectedNoteState by remember { mutableStateOf<NoteState?>(null) }
    var noteText by remember { mutableStateOf("") }

    var listOfNotesIdsForDeletion: List<Int>

    LaunchedEffect(key1 = backPressState) {
        if (backPressState == BackPressState.SecondTouch) {
            delay(1000)
            backPressState = BackPressState.InitialPress
        }
    }

    BackHandler(backPressState == BackPressState.InitialPress) {
        if (selectedNoteState != null) {
            selectedNoteState = null
        } else {
            backPressState = BackPressState.SecondTouch
            showSnack = true
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            HomeAppBar(
                selectAllNotes = {
                    areAllSelected = it
                    selectAllNotes.invoke(it)
                },
                noteListIsEmpty,
                areAllSelected,
                selectedNoteState != null
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (selectedNoteState != null) {
                    updateNote.invoke(selectedNoteState!!.note.copy(text = noteText))
                    selectedNoteState = null
                } else if (noteListHasSelectedItems) {
                    if (areAllSelected) {
                        deleteAllNotes.invoke()
                    } else {
                        listOfNotesIdsForDeletion =
                            (uiState.contentState as HomeViewContentState.Success)
                                .noteStates.filter { it.isSelected }
                                .map { it.note.id }
                        deleteNotes.invoke(listOfNotesIdsForDeletion)
                    }
                } else {
                    // create new note
                    navigationActions.navigateToEditor(0)
                }
            }) {
                if (selectedNoteState != null) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Confirm note changes"
                    )
                } else if (noteListHasSelectedItems) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete note"
                    )
                } else {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Add a note")
                }
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (uiState.contentState) {
                is HomeViewContentState.Error -> {
                    LaunchedEffect(uiState.contentState.message) {
                        snackbarHostState.showSnackbar(
                            message = uiState.contentState.message,
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                is HomeViewContentState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is HomeViewContentState.Success -> {
                    val listState = rememberLazyListState()

                    noteListIsEmpty = uiState.contentState.noteStates.isEmpty()
                    noteListHasSelectedItems =
                        uiState.contentState.noteStates.any { it.isSelected }
                    LaunchedEffect(showSnack) {
                        if (showSnack) {
                            snackbarHostState.showSnackbar(
                                message = "Press back again to exit",
                                duration = SnackbarDuration.Short
                            )
                            showSnack = false
                        }
                    }

                    areAllSelected =
                        uiState.contentState.noteStates.all { noteState -> noteState.isSelected }
                    SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            state = listState,
                        ) {
                            items(uiState.contentState.noteStates) {
                                AnimatedVisibility(
                                    visible = it != selectedNoteState,
                                    enter = fadeIn() + scaleIn(),
                                    exit = fadeOut() + scaleOut(),
                                    modifier = Modifier.animateItem()
                                ) {
                                    NoteItem(
                                        noteState = it,
                                        animatedVisibilityScope = this,
                                        editNote = {
                                            selectedNoteState = it
                                            noteText = it.note.text
                                        },
                                        selectNote = selectNote
                                    )
                                }
                            }
                        }

                        AnimatedContent(
                            targetState = selectedNoteState,
                            transitionSpec = {
                                fadeIn() togetherWith fadeOut()
                            },
                            label = "NoteEditor"
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .imePadding(),
                                contentAlignment = Alignment.Center
                            ) {
                                it?.let { noteState ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .sharedBounds(
                                                rememberSharedContentState(key = noteState.note.id.toString() + "-bound"),
                                                animatedVisibilityScope = this@AnimatedContent,
                                                clipInOverlayDuringTransition = OverlayClip(
                                                    RoundedCornerShape(16.dp)
                                                )
                                            )
                                            .padding(8.dp)
                                            .clip(RoundedCornerShape(16.dp)),
                                    ) {
                                        TextField(
                                            modifier = Modifier
                                                .fillMaxSize(),
                                            value = noteText,
                                            onValueChange = { noteText = it },
                                            keyboardOptions = KeyboardOptions(
                                                imeAction = ImeAction.Done
                                            ),
                                            keyboardActions = KeyboardActions(
                                                onDone = {
                                                    updateNote.invoke(noteState.note.copy(text = noteText))
                                                    selectedNoteState = null
                                                }
                                            ),
                                            colors = TextFieldDefaults.colors(
                                                focusedIndicatorColor = Color.Transparent,
                                                unfocusedIndicatorColor = Color.Transparent,
                                                disabledIndicatorColor = Color.Transparent
                                            ),
                                        )
                                    }
                                }
                            }
                        }

//                        NoteEditor(selectedNoteState, updateNote, noteText, deselectNote = {
//                            selectedNoteState = null
//                        })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SharedTransitionScope.NoteEditor(
    targetState: NoteState?,
    updateNote: (Note) -> Job,
    noteText: String?,
    deselectNote: () -> Unit
) {

}

enum class BackPressState { InitialPress, SecondTouch }