package com.example.waterreminder.database.reminder

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.waterreminder.R
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
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
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

        // nếu thời gian báo thức đã trôi qua, tăng ngày thêm 1
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar[Calendar.DAY_OF_MONTH] = calendar[Calendar.DAY_OF_MONTH] + 1
        }
        if (!isRecurring) {
            var toastText: String? = null
            try {
                toastText = String.format("One Time Alarm %s scheduled for %s at %02d:%02d", time, DayUtil.toDay(calendar[Calendar.DAY_OF_WEEK]), hour, minute, id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    alarmPendingIntent
                )
            }
        } else {
//            val toastText = String.format("Recurring Alarm %s scheduled for %s at %02d:%02d", time, getRecurringDaysText(), hour, minute, id)
//            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
            val RUN_DAILY = (24 * 60 * 60 * 1000).toLong()
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                RUN_DAILY,
                alarmPendingIntent
            )
        }

        scheduleNotification(context, calculateAlarmTriggerTime(calendar.timeInMillis))
        this.isStarted = true
    }
    fun scheduleNotification(context: Context, delayMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
            action = "SHOW_NOTIFICATION"
            putExtra("title", "Đến giờ uống nước rồi")
            putExtra("message", "Sau khi uống, chạm vào cốc để xác nhận")
        }
        val triggerTime = System.currentTimeMillis() + delayMillis
        println("levi12322 "+(delayMillis))

        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
        }
    }


    fun calculateAlarmTriggerTime(targetTimeInMillis: Long): Long {
        val currentTimeInMillis = System.currentTimeMillis()
         if (targetTimeInMillis > currentTimeInMillis) {
//             println("levi12322 "+(  targetTimeInMillis - currentTimeInMillis))
             return  targetTimeInMillis - currentTimeInMillis

        } else {
            println("levi1 "+0);
            // Nếu thời gian đã chọn nhỏ hơn thời gian hiện tại, trả về 0 hoặc một giá trị âm tương ứng để không đặt báo thức trong quá khứ.
            // Trong trường hợp này, bạn có thể xử lý theo ý muốn.
             return  0
        }
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




