package com.mitko.domain.usecase

import com.mitko.domain.note.Note
import com.mitko.domain.note.NoteRepository

class GetNoteUseCase(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(id: Int): Result<Note?> =
        Result.runCatching { noteRepository.getNote(id) }
}