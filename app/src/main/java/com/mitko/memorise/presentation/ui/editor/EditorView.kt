package com.mitko.memorise.presentation.ui.editor

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.mitko.domain.note.Note
import com.mitko.memorise.presentation.ui.components.EditorAppBar
import com.mitko.memorise.presentation.ui.navigation.AppNavigationActions
import kotlinx.coroutines.Job
import java.util.Date
import kotlin.reflect.KFunction1

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun EditorView(
    uiState: EditorViewState,
    navigationActions: AppNavigationActions,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    addNote: (Note) -> Job,
    deleteNote: KFunction1<Int, Job>,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    var noteId by remember { mutableIntStateOf(0) }
    var noteText by remember { mutableStateOf("") }

    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
        addNote.invoke(
            Note(
                noteId,
                Date(),
                noteText
            )
        )
    }

    BackHandler {
        navigationActions.navigateToHome()
    }

    when (uiState.contentState) {
        is EditorViewContentState.Error -> {
            LaunchedEffect(uiState.contentState.message) {
                snackbarHostState.showSnackbar(
                    message = uiState.contentState.message,
                    duration = SnackbarDuration.Short
                )
            }
        }

        is EditorViewContentState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is EditorViewContentState.Success -> {
            noteId = uiState.contentState.noteState.note.id
            if (noteText.isEmpty()) {
                noteText = uiState.contentState.noteState.note.text
            }
            with(sharedTransitionScope) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        EditorAppBar(
                            uiState.contentState.noteState.note.id,
                            deleteNote = {
                                deleteNote.invoke(noteId)
                                navigationActions.navigateToHome()
                            },
                            navigationActions
                        )
                    }
                ) { paddingValues ->
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .sharedElement(
                                rememberSharedContentState(key = uiState.contentState.noteState.note.id.toString() + "bound"),
                                animatedVisibilityScope = animatedContentScope,
                                boundsTransform = { _, _ ->
                                    tween(durationMillis = 500)
                                },
                                placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
                            )
                            .imePadding(),
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
                                    navigationActions.navigateToHome()
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
    }
}