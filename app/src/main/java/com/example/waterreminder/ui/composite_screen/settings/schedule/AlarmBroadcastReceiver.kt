package com.example.waterreminder.ui.composite_screen.settings.schedule

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import java.util.Calendar


class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title1 = intent.getStringExtra("hi")
        println("phuc123" + title1)
        if (  title1=="hi" ) {
            println(11111);
            val alarmIntent = Intent(context, AlarmService::class.java)
            context.stopService(alarmIntent)
            return;
        }
        println(1231235335);
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            println(123335);
            val toastText = String.format("Alarm Reboot")
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
            startRescheduleAlarmsService(context)
        } else {
            println(12333);
            val toastText = String.format("Alarm Received")
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
            if (!intent.getBooleanExtra(RECURRING, false)) {
                println(1231235332885);
                startAlarmService(context, intent)
            }
            run {
                if (alarmIsToday(intent)) {
                    println(1234588);
                    startAlarmService(context, intent)
                }
            }
        }

    }

    private fun alarmIsToday(intent: Intent): Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        when (calendar[Calendar.DAY_OF_WEEK]) {
            Calendar.MONDAY -> {
                return intent.getBooleanExtra(MONDAY, false)
            }

            Calendar.TUESDAY -> {
                return intent.getBooleanExtra(TUESDAY, false)
            }

            Calendar.WEDNESDAY -> {
                return intent.getBooleanExtra(WEDNESDAY, false)
            }

            Calendar.THURSDAY -> {
                return intent.getBooleanExtra(THURSDAY, false)
            }

            Calendar.FRIDAY -> {
                return intent.getBooleanExtra(FRIDAY, false)
            }

            Calendar.SATURDAY -> {
                return intent.getBooleanExtra(SATURDAY, false)
            }

            Calendar.SUNDAY -> {
                return intent.getBooleanExtra(SUNDAY, false)
            }
        }
        return false
    }

    private fun startAlarmService(context: Context, intent: Intent) {
        val intentService = Intent(context, AlarmService::class.java)
        intentService.putExtra(TITLE, intent.getStringExtra(TITLE))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }

    private fun startRescheduleAlarmsService(context: Context) {
        val intentService = Intent(context, RescheduleAlarmsService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }

    companion object {
        const val MONDAY = "MONDAY"
        const val TUESDAY = "TUESDAY"
        const val WEDNESDAY = "WEDNESDAY"
        const val THURSDAY = "THURSDAY"
        const val FRIDAY = "FRIDAY"
        const val SATURDAY = "SATURDAY"
        const val SUNDAY = "SUNDAY"
        const val RECURRING = "RECURRING"
        const val TITLE = "TITLE"
    }
}
