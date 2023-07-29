package com.example.waterreminder.ui.composite_screen.settings.schedule

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.example.waterreminder.Application.Companion.CHANNEL_ID
import com.example.waterreminder.R
import com.example.waterreminder.helper.PreferenceHelper
import com.example.waterreminder.ui.composite_screen.CompositeScreenActivity
import com.example.waterreminder.ui.composite_screen.settings.schedule.AlarmBroadcastReceiver.Companion.TITLE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class AlarmService : Service() {
    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    private var mediaPlayer: MediaPlayer? = null
    private var vibrator: Vibrator? = null
    private var handler: Handler? = null
    private var stopMusicRunnable: Runnable? = null

    override fun onCreate() {
        super.onCreate()
//
//        val sound =  preferenceHelper.getString(PreferenceHelper.PREF_SOUND)
//        if (sound != null) {
//            if(sound.length > 12){
//                try {
//                    mediaPlayer?.setDataSource(sound)
//                    mediaPlayer?.prepare()
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }else{
//           //     mediaPlayer = MediaPlayer.create(this,sound.trim().toInt())
//            }
//        }else{
//            mediaPlayer = MediaPlayer.create(this, R.raw.water_flow)
//        }
//        mediaPlayer!!.isLooping = true
//        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, CompositeScreenActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("It's time to drink water")
            .setContentText("After drinking, touch the cup to confirm")
            .setSmallIcon(R.drawable.img_water_splash)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .build()

//        mediaPlayer!!.start()
//        val pattern = longArrayOf(0, 100, 1000)
//        vibrator!!.vibrate(pattern, 0)
//        handler = Handler(Looper.getMainLooper())
//        stopMusicRunnable = Runnable {
//            mediaPlayer?.stop()
//            vibrator?.cancel()
//        }
//        handler?.postDelayed(stopMusicRunnable!!, 10000)

        startForeground(1, notification)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
