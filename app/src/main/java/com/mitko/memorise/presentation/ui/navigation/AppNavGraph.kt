@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.mitko.memorise.presentation.ui.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mitko.memorise.presentation.ui.editor.EditorScreen
import com.mitko.memorise.presentation.ui.home.HomeScreen

val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }
@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    navigationActions: AppNavigationActions,
) {
    SharedTransitionLayout {
        CompositionLocalProvider(
            LocalSharedTransitionScope provides this
        ) {
            NavHost(navController = navController, startDestination = Home) {
                composable<Home> {
                    CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
                        HomeScreen(navigationActions)
                    }
                }
                composable<Editor> {
                    val args = it.toRoute<Editor>()
                    EditorScreen(
                        args.noteId,
                        navigationActions,
                        this@SharedTransitionLayout,
                        this
                    )
                }
            }
        }
    }
}