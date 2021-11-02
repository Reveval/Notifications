package ru.startandroid.develop.p0187actionandreply

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import androidx.core.app.RemoteInput
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

const val CHANNEL_ID = "1"
const val DELETE_NOTIFICATION_ID = 1
const val EXTRA_ITEM_ID = "extraItemID"
const val EXTRA_TEXT_REPLY = "extraTextReply"
const val ITEM_ID = 156464131
const val ACTION_REPLY = "ru.startandroid.notifications.action_reply"

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
            Добавим кнопку Delete в уведомление. Сначала создаем PendingIntent, который будет
            вызван по нажатию на кнопку. Затем передаем его в метод addAction, а вместе с ним
            иконку и текст для кнопки.
         */
        val deleteIntent = Intent(this, MyService::class.java)
        deleteIntent.action = "ru.startandroid.notifications.action_delete"
        val deletePendingIntent = PendingIntent.getService(
            this, 0, deleteIntent,
            0
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        builder.apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle("Title")
            setContentText("Notification text")
            addAction(android.R.drawable.ic_delete, "Delete", deletePendingIntent)
        }

        createNotification(DELETE_NOTIFICATION_ID, builder)

        /*
            Reply
                Начиная с API 24 появилась возможность добавить в уведомление строку ввода. Это
                может быть удобно, например, в чат-приложениях. Пользователь сможет ответить на
                сообщение прямо из уведомления.
                Сначала создаем Intent и PendingIntent. Тут ничего нового. Мы будем вызывать сервис
                MyService и передавать ему itemId. В PendingIntent используем itemId в качестве
                requestCode.
         */
        val intent = Intent(this, MyService::class.java)
        intent.apply {
            action = ACTION_REPLY
            putExtra(EXTRA_ITEM_ID, ITEM_ID)
        }

        val replyPendingIntent = PendingIntent.getService(
            applicationContext, ITEM_ID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        /*
            Далее создаем RemoteInput. Здесь настраиваем все, что касается поля ввода, которое будет
                отображено в уведомлении. В конструкторе билдера необходимо указать ключ, который
                мы в дальнейшем будем использовать, чтобы из Bundle достать текст, который введет
                пользователь. В метод setLabel можно передать текст, который будет использован как
                hint (подсказка) в поле ввода.
         */
        val remoteInput = RemoteInput.Builder(EXTRA_TEXT_REPLY)
            .setLabel("Type message")
            .build()

        /*
            Создаем Action кнопку с помощью билдера. Передаем туда стандартный набор: иконку, текст
                и PendingIntent. А в метод addRemoteInput передаем ранее созданный RemoteInput. Это
                будет Action кнопка Reply, по нажатию на которую будет появляться строка ввода.
         */
        val action = NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_send, "Reply",
            replyPendingIntent
        )
            .addRemoteInput(remoteInput)
            .build()

        /*
            Далее используем созданный Action в билдере уведомления, создаем уведомление и
                отображаем его.
         */
        val builderReply = NotificationCompat.Builder(this, CHANNEL_ID)
        builderReply.apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle("Title")
            setContentText("Notification text")
            addAction(action)
        }

        createNotification(ITEM_ID, builderReply)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(notificationID: Int, builder: NotificationCompat.Builder) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID, "MyChannel",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.apply {
            description = "My Main Channel Description"
            enableLights(true)
            lightColor = Color.RED
            enableVibration(false)
        }

        notificationManager.createNotificationChannel(channel)
        val notification = builder.build()
        notificationManager.notify(notificationID, notification)
    }
}