package com.example.waterreminder.ui.composite_screen.settings.schedule

import android.app.PendingIntent
import android.content.Intent
import android.os.IBinder
import androidx.core.view.ContentInfoCompat.Flags
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
                    val intent = Intent(applicationContext, AlarmBroadcastReceiver::class.java)

                    a.schedule(applicationContext, PendingIntent.getBroadcast(applicationContext,a.id,intent,PendingIntent.FLAG_IMMUTABLE))
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