package com.example.waterreminder.database.sound

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.waterreminder.database.reminder.ReminderDao
import com.example.waterreminder.database.reminder.ReminderEntity


@Database(entities = [SoundEntity::class], version = 1, exportSchema = false)
abstract class SoundDatabase : RoomDatabase() {
    abstract val soundDao: SoundDao
}