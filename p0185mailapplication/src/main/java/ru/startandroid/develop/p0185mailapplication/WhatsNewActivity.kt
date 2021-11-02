package ru.startandroid.develop.p0185mailapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

const val CHANNEL_ID_FOR_WHATS_NEW = "2"
const val WHATS_NEW_NOTIFICATION_ID = 2

class WhatsNewActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_whats_new)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID_FOR_WHATS_NEW, "MyWhatsNewChannel",
            NotificationManager.IMPORTANCE_HIGH)
        channel.apply {
            description = "My What's New Channel Description"
            enableLights(true)
            lightColor = Color.RED
            enableVibration(false)
        }

        notificationManager.createNotificationChannel(channel)

        /*
            Создание уведомления выполняется по обычной схеме, только в Intent надо добавить пару
                флагов, чтобы Activity стартовало в новом пустом таске
         */
        val resultIntent = Intent(this, WhatsNewActivity::class.java)
        resultIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID_FOR_WHATS_NEW)
        val notification = builder.apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle("Details")
            setContentText("You got a new message")
            setContentIntent(resultPendingIntent)
        }.build()

        notificationManager.notify(WHATS_NEW_NOTIFICATION_ID, notification)
    }
}