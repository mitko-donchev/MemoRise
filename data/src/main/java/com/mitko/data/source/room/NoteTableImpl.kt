package com.mitko.data.source.room

import com.mitko.data.repository.note.NoteTable
import com.mitko.data.source.room.entity.NoteEntity
import com.mitko.domain.note.Note
import kotlinx.coroutines.flow.Flow

class NoteTableImpl(private val appDatabase: AppDatabase) : NoteTable {
    override suspend fun addNote(note: Note) =
        appDatabase.appDao().addNote(NoteEntity.fromNote(note))

    override suspend fun getNote(id: Int): NoteEntity? = appDatabase.appDao().getNoteById(id)

    override suspend fun getNotes(): Flow<List<NoteEntity>> = appDatabase.appDao().getNotes()

    override suspend fun updateNote(note: Note) =
        appDatabase.appDao().addNote(NoteEntity.fromNote(note))

    override suspend fun deleteNote(id: Int) = appDatabase.appDao().deleteNote(id)

    override suspend fun deleteAllNotes() = appDatabase.appDao().deleteAllNotes()
}