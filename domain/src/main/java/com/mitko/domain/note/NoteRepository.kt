package com.mitko.domain.note

import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun addNote(note: Note)
    suspend fun getNote(id: Int): Note?
    suspend fun getNotes(): Flow<List<Note>>
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(id: Int)
    suspend fun deleteAllNotes()
}