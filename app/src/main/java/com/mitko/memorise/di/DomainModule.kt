package com.mitko.memorise.di

import com.mitko.domain.note.NoteRepository
import com.mitko.domain.usecase.AddNoteUseCase
import com.mitko.domain.usecase.DeleteAllNotesUseCase
import com.mitko.domain.usecase.DeleteNoteUseCase
import com.mitko.domain.usecase.GetNoteUseCase
import com.mitko.domain.usecase.GetNotesUseCase
import com.mitko.domain.usecase.UpdateNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Provides
    fun provideAddNoteUseCase(noteRepository: NoteRepository) = AddNoteUseCase(noteRepository)

    @Provides
    fun provideGetNoteUseCase(noteRepository: NoteRepository) = GetNoteUseCase(noteRepository)

    @Provides
    fun provideGetNotesUseCase(noteRepository: NoteRepository) = GetNotesUseCase(noteRepository)

    @Provides
    fun provideUpdateNoteUseCase(noteRepository: NoteRepository) = UpdateNoteUseCase(noteRepository)

    @Provides
    fun provideDeleteNoteUseCase(noteRepository: NoteRepository) = DeleteNoteUseCase(noteRepository)

    @Provides
    fun provideDeleteAllNotesUseCase(noteRepository: NoteRepository) =
        DeleteAllNotesUseCase(noteRepository)
}