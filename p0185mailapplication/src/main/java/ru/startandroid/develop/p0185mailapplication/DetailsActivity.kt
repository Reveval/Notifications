package ru.startandroid.develop.p0185mailapplication

import android.app.NotificationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        //напишем логику для удаления уведомления
        val notificationId = intent.getLongExtra(EXTRA_ITEM_ID, 0).toInt()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }
}