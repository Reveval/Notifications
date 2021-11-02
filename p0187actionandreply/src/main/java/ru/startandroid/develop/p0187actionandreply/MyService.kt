package ru.startandroid.develop.p0187actionandreply

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput

//В MyService получим сообщение, которое пользователь отпрвил через reply notification
class MyService : Service() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (ACTION_REPLY == intent?.action) {
            var replyText: CharSequence? = null
            val results = RemoteInput.getResultsFromIntent(intent)
            if (results != null) {
                replyText = results.getCharSequence(EXTRA_TEXT_REPLY)
            }

            val itemId = intent.getIntExtra(EXTRA_ITEM_ID, 0)
            Log.d("myLogs", "replyText = $replyText, itemID = $itemId")

            //Можем выполнять любые операции с полученными replyText и itemId

            //Создаем новое уведомление
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                CHANNEL_ID, "MyChannel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)

            val repliedNotification = NotificationCompat.Builder(baseContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Replied")
                .build()

            notificationManager.notify(itemId, repliedNotification)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}