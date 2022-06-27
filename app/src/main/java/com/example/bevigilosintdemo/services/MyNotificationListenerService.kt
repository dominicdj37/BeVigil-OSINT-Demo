package com.example.bevigilosintdemo.services

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.bevigilosintdemo.R
import com.example.bevigilosintdemo.ui.activities.SplashActivity


class MyNotificationListenerService: NotificationListenerService() {
    private val CHANNEL_ID = "ForegroundService Kotlin"

    companion object {

        fun startService(context: Context, message: String) {
            val startIntent = Intent(context, MyNotificationListenerService::class.java)
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, MyNotificationListenerService::class.java)
            context.stopService(stopIntent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //do heavy work on a background thread
        createNotificationChannel()
        val notificationIntent = Intent(this, SplashActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("BeVigil")
            .setContentText("Monitoring App Installations")
            .setSmallIcon(R.drawable.ic_android_app)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
        //stopSelf();

        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID, "BeVigil Notifications",
                NotificationManager.IMPORTANCE_DEFAULT)

            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

    override fun onCreate() {
        super.onCreate()
    }
    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        if(isValidAppInstallNotification(sbn)) {
            createNotificationChannel()

            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("App Downloading")
                .setContentText("${sbn?.notification?.tickerText} downloading")
                .setSmallIcon(R.drawable.ic_android_app)
                .build()

            getSystemService(NotificationManager::class.java).notify(200, notification)

        }

    }

    private fun isValidAppInstallNotification(sbn: StatusBarNotification?): Boolean {
        return sbn?.packageName == "com.android.vending" && sbn.isOngoing && sbn.notification?.tickerText?.isNotBlank() == true
    }

}