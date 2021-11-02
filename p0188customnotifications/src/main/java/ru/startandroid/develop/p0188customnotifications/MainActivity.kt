package ru.startandroid.develop.p0188customnotifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

const val CHANNEL_ID = "1"
const val CUSTOM_NOTIFICATION_ID = 1
const val DECORATED_NOTIFICATION_ID = 2

//Исследуем возможность создания кастомных уведомлений
class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, MainActivity::class.java)
        val rootPendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        //Создаем RemoteViews из layout файла.
        val remoteViews = RemoteViews(packageName, R.layout.notification)
        remoteViews.apply {
            //Методом setTextViewText помещаем текст в View c id = R.id.textView.
            setTextViewText(R.id.textView, "Custom notification text")
            /*
                А методом setOnClickPendingIntent указываем PendingIntent, который будет вызван при
                    нажатии на View с id = R.id.root. В нашем примере root - это корневой
                    LinearLayout. Соответственно при нажатии на уведомление, будет использован этот
                    PendingIntent, чтобы запустить Activity/Service/BroadcastReceiver.
             */
            setOnClickPendingIntent(R.id.root, rootPendingIntent)
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContent(remoteViews)

        createNotification(CUSTOM_NOTIFICATION_ID, builder)

        /*
            Есть еще один, более новый, способ создания кастомного уведомления - использование
                стиля DecoratedCustomViewStyle.
         */
        val builderDecorated = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setCustomContentView(remoteViews)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())

        createNotification(DECORATED_NOTIFICATION_ID, builderDecorated)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(notificationID: Int, builder: NotificationCompat.Builder) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(CHANNEL_ID, "MyChannel",
            NotificationManager.IMPORTANCE_HIGH)
        channel.apply {
            description = "My Main Channel Description"
            enableLights(true)
            lightColor = Color.RED
            enableVibration(false)
        }

        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(notificationID, builder.build())
    }
}