package com.mitko.data.source.room.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mitko.domain.note.Note
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var date: Date,
    var text: String
) : Parcelable {
    companion object {
        fun fromNote(note: Note) = NoteEntity(id = note.id, date = note.date, text = note.text)

        fun toNote(noteEntity: NoteEntity) =
            Note(id = noteEntity.id, date = noteEntity.date, text = noteEntity.text)
    }
}