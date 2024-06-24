package com.mitko.memorise.presentation.ui.editor

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mitko.domain.note.Note
import com.mitko.domain.usecase.AddNoteUseCase
import com.mitko.domain.usecase.DeleteNoteUseCase
import com.mitko.domain.usecase.GetNoteUseCase
import com.mitko.domain.usecase.UpdateNoteUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

@HiltViewModel(assistedFactory = EditorViewModel.EditorViewModelFactory::class)
class EditorViewModel @AssistedInject constructor(
    @Assisted val noteId: Int,
    private val getNoteUseCase: GetNoteUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditorViewState(EditorViewContentState.Loading))
    val uiState = _uiState.asStateFlow()

    @AssistedFactory
    interface EditorViewModelFactory {
        fun create(noteId: Int): EditorViewModel
    }

    init {
        fetchNote()
    }

    fun fetchNote() = viewModelScope.launch {
        _uiState.update { it.copy(contentState = EditorViewContentState.Loading) }
        getNoteUseCase(noteId).fold({ note ->
            _uiState.update { editorViewState ->
                editorViewState.copy(
                    contentState = EditorViewContentState.Success(
                        noteState = if (note == null) {
                            NoteState(note = Note(0, Date(), ""))
                        } else {
                            NoteState(note = note)
                        }
                    )
                )
            }
        }, { error ->
            _uiState.update {
                it.copy(
                    contentState = EditorViewContentState.Error(
                        error.message ?: ""
                    )
                )
            }
        })
    }

    fun addNote(note: Note) = viewModelScope.launch {
        addNoteUseCase(note).onSuccess { result ->
            // Do something
        }
    }

    fun deleteNote(noteId: Int) = viewModelScope.launch {
        deleteNoteUseCase.invoke(noteId)
    }
}

@Immutable
data class EditorViewState(
    val contentState: EditorViewContentState
)

@Immutable
data class NoteState(val note: Note)

@Immutable
sealed class EditorViewContentState {
    data object Loading : EditorViewContentState()
    data class Success(val noteState: NoteState) : EditorViewContentState()
    data class Error(val message: String) : EditorViewContentState()
}