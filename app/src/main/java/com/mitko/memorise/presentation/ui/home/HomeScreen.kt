package com.mitko.memorise.presentation.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mitko.memorise.presentation.ui.navigation.AppNavigationActions

@Composable
fun HomeScreen(navigationActions: AppNavigationActions) {

    val homeViewModel = hiltViewModel<HomeViewModel>()
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    HomeView(
        uiState = uiState,
        navigationActions,
        homeViewModel::selectNote,
        homeViewModel::selectAllNotes,
        homeViewModel::deleteNote,
        homeViewModel::deleteNotes,
        homeViewModel::deleteAllNotes
    )
}