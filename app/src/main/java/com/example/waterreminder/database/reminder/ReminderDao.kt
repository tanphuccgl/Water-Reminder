package com.example.waterreminder.database.reminder

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertReminder(reminderEntity: ReminderEntity)

    @Query("SELECT * FROM Reminder ORDER BY time(time)")
    suspend fun getReminders(): List<ReminderEntity>

    @Query("SELECT * FROM Reminder")
    fun selectAll(): LiveData<List<ReminderEntity>>

    @Query("DELETE FROM Reminder WHERE time = :date")
    suspend fun deleteReminder(date: String)

    @Update
    suspend fun updaterReminder(reminderEntity: ReminderEntity)

}