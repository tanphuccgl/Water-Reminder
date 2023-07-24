package com.example.waterreminder.repository

import com.example.waterreminder.database.reminder.ReminderEntity
import com.example.waterreminder.database.sound.SoundDao
import com.example.waterreminder.database.sound.SoundEntity
import javax.inject.Inject

class SoundRepository @Inject constructor(private val soundDao: SoundDao){

    suspend fun getSounds(): List<SoundEntity>{
        return soundDao.getSounds()
    }

    suspend fun insertSound(soundEntity: SoundEntity){
        soundDao.insertSound(soundEntity)
    }

    suspend fun updateSound(soundEntity: SoundEntity){
        soundDao.updaterSound(soundEntity)
    }

//    suspend fun getSoundApp():List<SoundEntity>{
//        return soundDao.getSoundApp()
//    }
}