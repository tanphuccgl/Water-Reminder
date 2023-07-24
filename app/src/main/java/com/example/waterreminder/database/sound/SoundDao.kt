package com.example.waterreminder.database.sound

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.waterreminder.database.reminder.ReminderEntity

@Dao
interface SoundDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSound(soundEntity: SoundEntity)

    @Query("SELECT * FROM SoundEntity ORDER BY CASE WHEN file GLOB '[0-9]*' THEN 1 ELSE 2 END, file ASC")
    suspend fun getSounds(): List<SoundEntity>

    @Update
    suspend fun updaterSound(soundEntity: SoundEntity)
}