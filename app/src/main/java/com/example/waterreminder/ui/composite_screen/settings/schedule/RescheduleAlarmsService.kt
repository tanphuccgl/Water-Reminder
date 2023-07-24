package com.example.waterreminder.ui.composite_screen.settings.schedule

import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import com.example.waterreminder.repository.ReminderRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RescheduleAlarmsService @Inject constructor(
    private val reminderRepository: ReminderRepository
) : LifecycleService() {


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        reminderRepository.selectAll().observe(this){
            for(a in it){
                if(a.isStarted){
                    a.schedule(applicationContext)
                }
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent!!)
        return null
    }
}