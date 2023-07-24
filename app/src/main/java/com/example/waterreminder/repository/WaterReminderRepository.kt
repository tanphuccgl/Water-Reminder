package com.example.waterreminder.repository

import androidx.lifecycle.LiveData
import com.example.waterreminder.database.WaterReminderDao
import com.example.waterreminder.database.WaterReminderEntity
import javax.inject.Inject

class WaterReminderRepository @Inject constructor(private val waterReminderDao: WaterReminderDao) {

    val allWaterReminders: LiveData<List<WaterReminderEntity>>
        get() = waterReminderDao.selectAll()

    suspend fun getWaterReminders(): List<WaterReminderEntity> {
        return waterReminderDao.getWaterReminders()
    }

    suspend fun insertWaterReminder(waterReminderEntity: WaterReminderEntity){
        waterReminderDao.insertWaterReminder(waterReminderEntity)
    }

    suspend fun updateWaterReminder(waterReminderEntity: WaterReminderEntity){
        waterReminderDao.updateWaterReminder(waterReminderEntity)
    }

    suspend fun getWaterRemindersByDate(date:String){
        waterReminderDao.getWaterRemindersByCheckedDate(date)
    }

    suspend fun deleteWaterReminder(date: String){
        waterReminderDao.deleteWaterReminder(date)
    }
}