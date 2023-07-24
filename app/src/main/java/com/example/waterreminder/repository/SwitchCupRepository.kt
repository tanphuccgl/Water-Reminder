package com.example.waterreminder.repository

import com.example.waterreminder.database.cup.SwitchCup
import com.example.waterreminder.database.cup.SwitchCupDao
import com.example.waterreminder.database.reminder.ReminderDao
import com.example.waterreminder.database.reminder.ReminderEntity
import javax.inject.Inject

class SwitchCupRepository @Inject constructor(private val switchCupDao: SwitchCupDao){

    suspend fun getSwitchCup(): List<SwitchCup>{
        return switchCupDao.getSwitchCup()
    }

    suspend fun insertSwitchCup(switchCup: SwitchCup){
        switchCupDao.insertSwitchCup(switchCup)
    }

    suspend fun updateSwitchCup(switchCup: SwitchCup){
        switchCupDao.updaterSwitchCup(switchCup)
    }

    suspend fun deleteSwitchCup(quantity: String){
        switchCupDao.deleteSwitchCup(quantity)
    }

    suspend fun getSelectedCup(): SwitchCup? {
        return switchCupDao.getSelectedCup()
    }

}