package com.mitko.memorise.presentation.ui.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mitko.domain.note.Note
import com.mitko.domain.usecase.DeleteAllNotesUseCase
import com.mitko.domain.usecase.DeleteNoteUseCase
import com.mitko.domain.usecase.GetNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val deleteAllNotesUseCase: DeleteAllNotesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeViewState(HomeViewContentState.Loading))
    val uiState = _uiState.asStateFlow()

    init {
        fetchNotes()
    }

    fun selectNote(noteId: Int) = viewModelScope.launch {
        (_uiState.value.contentState as HomeViewContentState.Success).noteStates.let { noteStates ->
            _uiState.update { homeViewState ->
                homeViewState.copy(
                    contentState = HomeViewContentState.Success(
                        noteStates = noteStates.map {
                            if (it.note.id == noteId) {
                                it.copy(isSelected = !it.isSelected)
                            } else {
                                it.copy()
                            }
                        }
                    )
                )
            }
        }
    }

    fun selectAllNotes(shouldSelectAll: Boolean) = viewModelScope.launch {
        (_uiState.value.contentState as HomeViewContentState.Success).noteStates.let { noteStates ->
            _uiState.update { homeViewState ->
                homeViewState.copy(
                    contentState = HomeViewContentState.Success(
                        noteStates = noteStates.map {
                            it.copy(isSelected = shouldSelectAll)
                        }
                    )
                )
            }
        }
    }

    fun deleteNote(noteId: Int) = viewModelScope.launch {
        deleteNoteUseCase.invoke(noteId)
    }

    fun deleteNotes(notesIds: List<Int>) = viewModelScope.launch {
        notesIds.forEach {
            deleteNoteUseCase.invoke(it)
        }
    }

    fun deleteAllNotes() = viewModelScope.launch { deleteAllNotesUseCase.invoke() }

    private fun fetchNotes() = viewModelScope.launch {
        _uiState.update { it.copy(contentState = HomeViewContentState.Loading) }
        getNotesUseCase().fold({ notes ->
            notes.collectLatest {
                _uiState.update { homeViewState ->
                    homeViewState.copy(
                        contentState = HomeViewContentState.Success(
                            noteStates = it.map {
                                NoteState(note = it, isSelected = false)
                            }
                        )
                    )
                }
            }
        }, { error ->
            _uiState.update {
                it.copy(
                    contentState = HomeViewContentState.Error(
                        error.message ?: ""
                    )
                )
            }
        })
    }
}

@Immutable
data class HomeViewState(
    val contentState: HomeViewContentState
)

@Immutable
data class NoteState(val note: Note, val isSelected: Boolean)

@Immutable
sealed class HomeViewContentState {
    data object Loading : HomeViewContentState()
    data class Success(val noteStates: List<NoteState>) : HomeViewContentState()
    data class Error(val message: String) : HomeViewContentState()
}