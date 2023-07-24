package com.example.waterreminder.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WaterReminderEntity::class], version = 1, exportSchema = false)
abstract class WaterReminderDatabase : RoomDatabase() {
    abstract val waterReminderDao:WaterReminderDao

}