package com.mitko.memorise.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import com.mitko.memorise.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(
    selectAllNotes: (Boolean) -> Unit,
    isNoteListEmpty: Boolean,
    areAllSelected: Boolean
) {
    val haptic = LocalHapticFeedback.current

    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name)
            )
        },
        actions = {
            // Select Action
            if (!isNoteListEmpty) {
                IconButton(onClick = {
                    selectAllNotes.invoke(!areAllSelected)
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }) {
                    if (areAllSelected) {
                        Icon(
                            imageVector = Icons.Filled.Deselect,
                            contentDescription = "Deselect all notes"
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.SelectAll,
                            contentDescription = "Select all notes"
                        )
                    }
                }
            }
        },
    )
}