package com.example.waterreminder.ui.composite_screen.settings.schedule

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.waterreminder.R
import java.util.Calendar
import java.util.Random


class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "SHOW_NOTIFICATION") {
            val title = intent.getStringExtra("title")
            val message = intent.getStringExtra("message")

            showNotification(context, title.toString(), message.toString())
        }
    }

    // Rest of the showNotification function and other code


    fun showNotification(context: Context, title: String, message: String) {
        val channelId = "ALARM_SERVICE_CHANNEL"
        val random = Random()
        val randomId = random.nextInt()// Định nghĩa một ID cho kênh thông báo
        val notificationId = randomId  // Định nghĩa một ID cho thông báo

        // Tạo một thông báo
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.icon_clock)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Hiển thị thông báo
        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}