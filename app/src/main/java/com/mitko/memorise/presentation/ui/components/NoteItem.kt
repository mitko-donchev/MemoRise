@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.mitko.memorise.presentation.ui.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mitko.memorise.presentation.ui.home.NoteState

@Composable
fun SharedTransitionScope.NoteItem(
    noteState: NoteState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    editNote: () -> Unit,
    selectNote: (Int) -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    val shapeForSharedElement = RoundedCornerShape(16.dp)

    var currentState: SelectedState by remember { mutableStateOf(SelectedState.Deselected) }

    val transition = updateTransition(targetState = currentState, label = "select animation")
    val scale: Float by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 200) }, label = ""
    ) { state ->
        // When the item is selected ,reduce its size
        // by 5% or make its size 0.95 of its original size
        // Change this value to see effect
        if (state == SelectedState.Selected) {
            0.95f
        } else {
            // When the item is deselected,
            // make it of its original size
            1f
        }
    }

    currentState = if (noteState.isSelected) SelectedState.Selected else SelectedState.Deselected

    Box(
        modifier = Modifier
            .sharedBounds(
                rememberSharedContentState(key = noteState.note.id.toString() + "-bound"),
                animatedVisibilityScope = animatedVisibilityScope,
                clipInOverlayDuringTransition = OverlayClip(shapeForSharedElement)
            )
            .padding(4.dp)
            .scale(scale)
            .shadow(
                elevation = if (currentState == SelectedState.Selected) 2.dp else 4.dp,
                shape = shapeForSharedElement
            )
            .background(
                color = if (currentState == SelectedState.Selected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.background
                }, shape = shapeForSharedElement
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        editNote.invoke()
                    },
                    onLongPress = {
                        selectNote.invoke(noteState.note.id)
                        currentState =
                            if (currentState == SelectedState.Deselected) SelectedState.Selected else SelectedState.Deselected
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                )
            }
            .clip(shapeForSharedElement)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = noteState.note.text,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

enum class SelectedState { Selected, Deselected }