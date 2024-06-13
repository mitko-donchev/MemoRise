package com.mitko.memorise.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.mitko.memorise.presentation.theme.PlainNotesTheme
import com.mitko.memorise.presentation.ui.navigation.AppNavGraph
import com.mitko.memorise.presentation.ui.navigation.AppNavigationActions

@Composable
fun NoteApp() {
    PlainNotesTheme {
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            AppNavigationActions(navController)
        }

        val coroutineScope = rememberCoroutineScope()

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AppNavGraph(
                navController = navController,
                navigationActions = navigationActions,
            )
        }
    }
}