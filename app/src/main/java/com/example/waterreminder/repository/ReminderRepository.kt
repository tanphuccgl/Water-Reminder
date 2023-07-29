package com.example.waterreminder.repository

import androidx.lifecycle.LiveData
import com.example.waterreminder.database.reminder.ReminderDao
import com.example.waterreminder.database.reminder.ReminderEntity
import javax.inject.Inject

class ReminderRepository @Inject constructor(private val reminderDao: ReminderDao){

    private val alarmsLiveData: LiveData<List<ReminderEntity>>? = null

    suspend fun getReminders(): List<ReminderEntity>{
        return reminderDao.getReminders()
    }

    suspend fun insertReminder(reminderEntity: ReminderEntity){
        reminderDao.insertReminder(reminderEntity)
    }

    suspend fun updateReminder(reminderEntity: ReminderEntity){
        reminderDao.updaterReminder(reminderEntity)
    }

    suspend fun deleteReminder(date: String){
        reminderDao.deleteReminder(date)
    }
    fun selectAll(): LiveData<List<ReminderEntity>> {
        return reminderDao.selectAll()
    }

    suspend fun deleteReminderID(id: Int) {
        return reminderDao.deleteReminderID(id)
    }
}