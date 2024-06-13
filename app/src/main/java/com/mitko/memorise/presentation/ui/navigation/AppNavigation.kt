package com.mitko.memorise.presentation.ui.navigation

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class Editor(val noteId: Int)

/**
 * Models the navigation actions in the app.
 */
class AppNavigationActions(private val navController: NavController) {
    fun navigateToHome() = navController.navigate(Home) {
        popUpTo(Home) { inclusive = true }
    }

    fun navigateToEditor(noteId: Int) = navController.navigate(Editor(noteId)) {
        popUpTo(Home) { inclusive = true }
    }
}