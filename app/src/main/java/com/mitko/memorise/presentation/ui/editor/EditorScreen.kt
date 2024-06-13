package com.mitko.memorise.presentation.ui.editor

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.mitko.memorise.presentation.ui.navigation.AppNavigationActions

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun EditorScreen(
    noteId: Int,
    navigationActions: AppNavigationActions,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {

    val editorViewModel =
        hiltViewModel<EditorViewModel, EditorViewModel.EditorViewModelFactory> { factory ->
            factory.create(noteId)
        }
    val uiState by editorViewModel.uiState.collectAsState()

    EditorView(
        uiState = uiState,
        navigationActions,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        editorViewModel::addNote,
        editorViewModel::deleteNote,
    )
}