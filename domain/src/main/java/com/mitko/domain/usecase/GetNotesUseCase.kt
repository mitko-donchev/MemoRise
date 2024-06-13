package com.mitko.domain.usecase

import com.mitko.domain.note.Note
import com.mitko.domain.note.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetNotesUseCase(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(): Result<Flow<List<Note>>> =
        Result.runCatching { noteRepository.getNotes() }
}