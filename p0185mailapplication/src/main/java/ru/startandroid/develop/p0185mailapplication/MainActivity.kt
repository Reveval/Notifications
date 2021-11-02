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

const val CHANNEL_ID_FOR_MAIN = "1"
const val EXTRA_ITEM_ID = "myStringConst"
const val ITEM_ID = 12345678910L
const val MAIN_NOTIFICATION_ID = ITEM_ID.toInt()

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(CHANNEL_ID_FOR_MAIN, "MyMainChannel",
            NotificationManager.IMPORTANCE_HIGH)
        channel.apply {
            description = "My Main Channel Description"
            enableLights(true)
            lightColor = Color.RED
            enableVibration(false)
        }

        notificationManager.createNotificationChannel(channel)

        /*
            Создаем обычный Intent для открытия DetailsActivity. Передаем туда id, чтобы Activity
            знало, какую информацию ему надо отобразить.
         */
        val resultIntent = Intent(this, DetailsActivity::class.java)
        resultIntent.putExtra(EXTRA_ITEM_ID, ITEM_ID)

        /*
            Затем создаем TaskStackBuilder - инструмент, который поможет нам сформировать
                последовательность вызовов Activity. Нам надо, чтобы сначала запустилось
                родительское Activity для DetailsActivity (т.е. MainActivity), а затем и само
                DetailsActivity.
         */
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.apply {
            /*
                Вызываем метод addParentStack и указываем в нем DetailsActivity, т.е. просим в стек
                    вызовов добавить Activity, которое является родительским для DetailsActivity.
                    TaskStackBuilder идет в манифест и видит, что для DetailsActivity в качестве
                    родительского (parentActivityName) прописано MainActivity. TaskStackBuilder
                    добавляет MainActivity в стек вызовов.
             */
            addParentStack(DetailsActivity::class.java)
            /*
                В addNextIntent мы просто передаем Intent для запуска DetailsActivity.
                    TaskStackBuilder добавит его в свой стек вызовов.
             */
            addNextIntent(resultIntent)
        }

        /*
            В итоге TaskStackBuilder содержит в стеке вызовов два Activity: сначала MainActivity,
                а затем DetailsActivity. Методом getPendingIntent он формирует PendingIntent,
                который мы сможем передать в билдер уведомления. И по нажатию на уведомление будут
                открыты Activity, которые были в стеке вызовов, сформированном в TaskStackBuilder.
         */
        val resultPendingIntent = stackBuilder.getPendingIntent(0,
            PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID_FOR_MAIN)
        val notification = builder.apply {
            setSmallIcon(R.mipmap.ic_launcher)
            setContentTitle("Title")
            setContentText("NotificationText")
            setContentIntent(resultPendingIntent)
        }.build()

        notificationManager.notify(MAIN_NOTIFICATION_ID, notification)
    }
}