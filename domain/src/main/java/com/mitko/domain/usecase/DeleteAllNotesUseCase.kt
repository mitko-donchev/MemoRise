package com.mitko.domain.usecase

import com.mitko.domain.note.NoteRepository

class DeleteAllNotesUseCase(private val noteRepository: NoteRepository) {
    suspend operator fun invoke() = Result.runCatching { noteRepository.deleteAllNotes() }
}