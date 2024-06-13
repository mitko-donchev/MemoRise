package com.mitko.data.repository.note

import com.mitko.data.source.room.entity.NoteEntity
import com.mitko.domain.note.Note
import com.mitko.domain.note.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(private val noteTable: NoteTable) : NoteRepository {
    override suspend fun addNote(note: Note) = noteTable.addNote(note)
    override suspend fun getNote(id: Int): Note? {
        val noteEntity = noteTable.getNote(id) ?: return null
        return NoteEntity.toNote(noteEntity)
    }

    override suspend fun getNotes(): Flow<List<Note>> = noteTable.getNotes()
        .map { noteEntities ->
            noteEntities.map { NoteEntity.toNote(it) }
        }

    override suspend fun updateNote(note: Note) = noteTable.updateNote(note)
    override suspend fun deleteNote(id: Int) = noteTable.deleteNote(id)
    override suspend fun deleteAllNotes() = noteTable.deleteAllNotes()
}