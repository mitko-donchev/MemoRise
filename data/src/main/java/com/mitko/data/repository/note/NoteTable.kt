package com.mitko.data.repository.note

import com.mitko.data.source.room.entity.NoteEntity
import com.mitko.domain.note.Note
import kotlinx.coroutines.flow.Flow

interface NoteTable {
    suspend fun addNote(note: Note)
    suspend fun getNote(id: Int): NoteEntity?
    suspend fun getNotes(): Flow<List<NoteEntity>>
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(id: Int)
    suspend fun deleteAllNotes()
}