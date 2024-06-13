package com.mitko.data.source.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mitko.data.source.room.converter.DateConverter
import com.mitko.data.source.room.dao.AppDao
import com.mitko.data.source.room.entity.NoteEntity

@TypeConverters(DateConverter::class)
@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}