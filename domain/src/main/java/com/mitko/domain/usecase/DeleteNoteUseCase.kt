package com.mitko.domain.usecase

import com.mitko.domain.note.NoteRepository

class DeleteNoteUseCase(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(id: Int) = Result.runCatching { noteRepository.deleteNote(id) }
}