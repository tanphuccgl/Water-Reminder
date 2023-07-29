package com.example.waterreminder.database.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.waterreminder.ui.composite_screen.settings.schedule.AlarmBroadcastReceiver
import com.example.waterreminder.ui.composite_screen.settings.schedule.AlarmBroadcastReceiver.Companion.FRIDAY
import com.example.waterreminder.ui.composite_screen.settings.schedule.AlarmBroadcastReceiver.Companion.MONDAY
import com.example.waterreminder.ui.composite_screen.settings.schedule.AlarmBroadcastReceiver.Companion.RECURRING
import com.example.waterreminder.ui.composite_screen.settings.schedule.AlarmBroadcastReceiver.Companion.SATURDAY
import com.example.waterreminder.ui.composite_screen.settings.schedule.AlarmBroadcastReceiver.Companion.SUNDAY
import com.example.waterreminder.ui.composite_screen.settings.schedule.AlarmBroadcastReceiver.Companion.THURSDAY
import com.example.waterreminder.ui.composite_screen.settings.schedule.AlarmBroadcastReceiver.Companion.TITLE
import com.example.waterreminder.ui.composite_screen.settings.schedule.AlarmBroadcastReceiver.Companion.TUESDAY
import com.example.waterreminder.ui.composite_screen.settings.schedule.AlarmBroadcastReceiver.Companion.WEDNESDAY
import com.example.waterreminder.ui.composite_screen.settings.schedule.DayUtil
import com.example.waterreminder.ui.composite_screen.settings.schedule.NotificationBroadcastReceiver
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Entity(tableName = "Reminder")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    @ColumnInfo(name = "time")
    var time: String,
    @ColumnInfo(name = "isMonday")
    var isMonday: Boolean = true,
    @ColumnInfo(name = "isTuesday")
    var isTuesday: Boolean = true,
    @ColumnInfo(name = "isWednesday")
    var isWednesday: Boolean = true,
    @ColumnInfo(name = "isThursday")
    var isThursday: Boolean = true,
    @ColumnInfo(name = "isFriday")
    var isFriday: Boolean = true,
    @ColumnInfo(name = "isSaturday")
    var isSaturday: Boolean = true,
    @ColumnInfo(name = "isSunday")
    var isSunday: Boolean = true,
    @ColumnInfo(name = "isCheck")
    var isCheck: Boolean = true,
    @ColumnInfo(name = "isRecurring")
    var isRecurring: Boolean = true,
    @ColumnInfo(name = "isStarted")
    var isStarted: Boolean = true,
){
    fun schedule(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        intent.action ="BOOT_COMPLETED"
        intent.putExtra(RECURRING, isRecurring)
        intent.putExtra(MONDAY, isMonday)
        intent.putExtra(TUESDAY, isTuesday)
        intent.putExtra(WEDNESDAY, isWednesday)
        intent.putExtra(THURSDAY, isThursday)
        intent.putExtra(FRIDAY, isFriday)
        intent.putExtra(SATURDAY, isSaturday)
        intent.putExtra(SUNDAY, isSunday)
        intent.putExtra(TITLE, time)

        val alarmPendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_IMMUTABLE)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val timeString = time
        // Định dạng đầu vào của chuỗi thời gian
        val inputFormat = SimpleDateFormat("hh:mm aa", Locale.US)

        // Định dạng đầu ra để lấy giờ và phút
        val outputFormat = SimpleDateFormat("HH:mm", Locale.US)

        // Chuyển đổi chuỗi thời gian vào đối tượng Date
        val date = inputFormat.parse(timeString)

        // Lấy giờ và phút từ đối tượng Date
        val formattedTime = outputFormat.format(date)

        val hour = formattedTime.substring(0, 2).toInt()
        val minute = formattedTime.substring(3).toInt()

        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
            val RUN_DAILY = (24 * 60 * 60 * 1000).toLong()
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                RUN_DAILY,
                alarmPendingIntent
            )



        this.isStarted = true
    }



    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(alarmPendingIntent)
        this.isStarted = false
        val toastText = String.format("Alarm cancelled for %02d:%02d with id %d", 1, 2, id)
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
        Log.i("cancel", toastText)
    }


    private fun getRecurringDaysText(): String? {
        if (!isRecurring) {
            return null
        }
        var days = ""
        if (isMonday) {
            days += "Mo "
        }
        if (isTuesday) {
            days += "Tu "
        }
        if (isWednesday) {
            days += "We "
        }
        if (isThursday) {
            days += "Th "
        }
        if (isFriday) {
            days += "Fr "
        }
        if (isSaturday) {
            days += "Sa "
        }
        if (isSunday) {
            days += "Su "
        }
        return days
    }




    }




