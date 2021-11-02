package ru.startandroid.develop.p0190notificationchannels

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi

const val GROUP_USER_A_ID = "user_a_id"
const val GROUP_USER_B_ID = "user_b_id"
const val CHANNEL_ID_MAIL = "channel_mail_id"
const val CHANNEL_ID_EVENTS = "channel_events_id"

//Работаем с Notification Channel
class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Создаем группу - это просто способ визуально разделить каналы в настройках.
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannelGroup(NotificationChannelGroup(
            GROUP_USER_A_ID, "User A"))
        notificationManager.createNotificationChannelGroup(NotificationChannelGroup(
            GROUP_USER_B_ID, "User B"))

        //Теперь создаем Notifications Channel и добавляем их в одну группу
        createNotificationChannel(CHANNEL_ID_MAIL, "Mail", GROUP_USER_A_ID,
            notificationManager)
        createNotificationChannel(CHANNEL_ID_EVENTS, "Events", GROUP_USER_A_ID,
            notificationManager)
        createNotificationChannel(CHANNEL_ID_MAIL, "Mail", GROUP_USER_B_ID,
            notificationManager)
        createNotificationChannel(CHANNEL_ID_EVENTS, "Events", GROUP_USER_B_ID,
            notificationManager)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        channelID: String,
        channelName: String,
        groupID: String,
        notificationManager: NotificationManager
    ) {
        val channel = NotificationChannel(channelID, channelName,
            NotificationManager.IMPORTANCE_HIGH)
        channel.apply {
            description = "My $channelName description"
            enableLights(true)
            lightColor = Color.RED
            enableVibration(false)
            /*
                при создании канала используем метод setGroup, чтобы указать какой группе будет
                    принадлежать канал.
             */
            group = groupID
        }
        notificationManager.createNotificationChannel(channel)
    }
}