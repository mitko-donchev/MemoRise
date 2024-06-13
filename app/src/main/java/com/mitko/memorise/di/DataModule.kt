package com.mitko.memorise.di

import android.content.Context
import androidx.room.Room
import com.mitko.data.repository.note.NoteRepositoryImpl
import com.mitko.data.repository.note.NoteTable
import com.mitko.data.source.room.AppDatabase
import com.mitko.data.source.room.NoteTableImpl
import com.mitko.domain.note.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val APP_DB = "app.db"

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideNoteRepository(noteTable: NoteTable): NoteRepository =
        NoteRepositoryImpl(noteTable)

    @Provides
    @Singleton
    fun provideNoteTable(appDatabase: AppDatabase): NoteTable =
        NoteTableImpl(appDatabase)
}

@Module
@InstallIn(SingletonComponent::class)
object SourceModule {
    @Provides
    @Singleton
    fun provideNoteTable(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, APP_DB)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

    @Provides
    fun provideAppDao(appDatabase: AppDatabase) = appDatabase.appDao()
}