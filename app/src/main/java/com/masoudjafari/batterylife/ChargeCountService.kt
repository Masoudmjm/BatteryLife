package com.masoudjafari.batterylife

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class ChargeCountService : Service(){
    private val CHANNEL_ID = "chargeCount"
    private lateinit var notifManager: NotificationManager
    private lateinit var functions: Functions

    override fun onCreate() {
        super.onCreate()
        functions = Functions(this)
        functions.registerPowerReceiver()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        makeNotification()
        return START_NOT_STICKY
    }

    private fun makeNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Service Channel"
            val channelDescription = "ChargeTimes Channel"
            val channelImportance = NotificationManager.IMPORTANCE_DEFAULT
            val notifChannel = NotificationChannel(CHANNEL_ID, channelName, channelImportance)
            notifChannel.description = channelDescription
            notifManager = getSystemService(NotificationManager::class.java)
            notifManager.createNotificationChannel(notifChannel)
        }

        val sNotifBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("charge counter")
            .setContentText("charge count is ${functions.getSavedChargeCount()}")

        val servNotification: Notification = sNotifBuilder.build()

        startForeground(1, servNotification)
    }
}