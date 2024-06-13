package com.mitko.memorise.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mitko.memorise.R
import com.mitko.memorise.presentation.ui.navigation.AppNavigationActions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorAppBar(
    noteId: Int,
    deleteNote: () -> Unit,
    navigationActions: AppNavigationActions
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = if (noteId == 0) {
                    stringResource(R.string.new_note)
                } else {
                    stringResource(R.string.edit_note)
                }
            )
        },
        navigationIcon = {
            IconButton(onClick = { navigationActions.navigateToHome() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigate back"
                )
            }
        },
        actions = {
            IconButton(onClick = {
                deleteNote.invoke()
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Note"
                )
            }
        },
    )
}