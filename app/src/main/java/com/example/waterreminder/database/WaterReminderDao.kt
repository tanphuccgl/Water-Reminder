package com.example.waterreminder.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface WaterReminderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWaterReminder(waterReminder: WaterReminderEntity)

    @Query("select * from WaterReminder")
    suspend fun getWaterReminders(): List<WaterReminderEntity>

    @Query("select * from WaterReminder")
    fun selectAll(): LiveData<List<WaterReminderEntity>>

    @Query("SELECT * FROM WaterReminder WHERE checkedDate = :date")
    suspend fun getWaterRemindersByCheckedDate(date: String): List<WaterReminderEntity>

    @Query("DELETE FROM WaterReminder WHERE checkedDate = :date")
    suspend fun deleteWaterReminder(date: String)

    @Update
    suspend fun updateWaterReminder(waterReminder: WaterReminderEntity)
}