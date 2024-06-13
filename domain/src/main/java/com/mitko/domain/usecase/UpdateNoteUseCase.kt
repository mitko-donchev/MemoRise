package com.mitko.domain.usecase

import com.mitko.domain.note.Note
import com.mitko.domain.note.NoteRepository

class UpdateNoteUseCase(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(note: Note) = Result.runCatching { noteRepository.updateNote(note) }
}