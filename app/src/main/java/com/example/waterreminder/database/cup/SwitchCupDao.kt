package com.example.waterreminder.database.cup

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.waterreminder.database.reminder.ReminderEntity

@Dao
interface SwitchCupDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSwitchCup(switchCup: SwitchCup)

    @Query("SELECT * FROM SwitchCup ORDER BY CAST(SUBSTR(quantityWater, 1, INSTR(quantityWater, ' ') - 1) AS INTEGER)")
    suspend fun getSwitchCup(): List<SwitchCup>

    @Query("SELECT * FROM SwitchCup")
    fun selectAll(): LiveData<List<SwitchCup>>

    @Query("DELETE FROM SwitchCup WHERE quantityWater = :quantityWater")
    suspend fun deleteSwitchCup(quantityWater: String)

    @Update
    suspend fun updaterSwitchCup(switchCup: SwitchCup)

    @Query("SELECT * FROM SwitchCup WHERE isSelected = 1")
    suspend fun getSelectedCup(): SwitchCup?
}