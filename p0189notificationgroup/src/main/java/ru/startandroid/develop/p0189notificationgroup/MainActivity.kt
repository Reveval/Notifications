package ru.startandroid.develop.p0189notificationgroup

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

const val CHANNEL_ID = "myCustomChannelId"
const val NOTIFICATION_ID = 145646
const val GROUP_KEY = "myCustomGroupKey"

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(CHANNEL_ID, "MyCustomChannel",
            NotificationManager.IMPORTANCE_HIGH)
        channel.apply {
            description = "My Custom Channel Description"
            enableLights(true)
            lightColor = Color.RED
            enableVibration(false)
        }
        notificationManager.createNotificationChannel(channel)

        //создаем группу для уведомлений
        val builderForGroup = NotificationCompat.Builder(this, CHANNEL_ID)
        val group = builderForGroup.apply {
            setSmallIcon(R.mipmap.ic_launcher)
            /*
                setContentInfo позволяет отобразить  какое-то текстовое описание для группы, чтобы
                    пользователь понимал, о чем она.
             */
            setContentInfo("user_mail.com")
            /*
                В setGroup передаем String ключ группы. Его же мы используем при создании
                    уведомлений, которые должны попадать в эту группу.
             */
            setGroup(GROUP_KEY)
            //В setGroupSummary указываем true. Это означает, что уведомление является группой.
            setGroupSummary(true)
        }.build()

        for (index in 1..3) {
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            val notification = builder.apply {
                setSmallIcon(R.mipmap.ic_launcher)
                setContentTitle("Sender $index")
                setContentText("Subject text $index")
                /*
                    В методе setGroup указываем String ключ группы, чтобы система знала, в какую группу
                        помещать это уведомление.
                 */
                setGroup(GROUP_KEY)
            }.build()
            notificationManager.notify(index, notification)
            notificationManager.notify(-100, group)
        }
    }
}