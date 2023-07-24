package com.example.waterreminder.database.cup

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.waterreminder.database.reminder.ReminderDao
import com.example.waterreminder.database.reminder.ReminderEntity

@Database(entities = [SwitchCup::class], version = 1, exportSchema = false)
abstract class SwitchCupDatabase : RoomDatabase() {
    abstract val switchCupDao:SwitchCupDao

}